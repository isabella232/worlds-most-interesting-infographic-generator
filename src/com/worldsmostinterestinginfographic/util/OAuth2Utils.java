package com.worldsmostinterestinginfographic.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public enum OAuth2Utils {
  INSTANCE;

  private static final Logger log = Logger.getLogger(OAuth2Utils.class.getName());

  public static String requestAccessToken(String tokenEndpoint) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      // Exchange authorization code for access token
      HttpPost httpPost = new HttpPost(tokenEndpoint);
      HttpResponse httpResponse = httpClient.execute(httpPost);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      String line = bufferedReader.readLine();

      // Detect error message
      if (line.toLowerCase().contains("\"error\"")) {
        log.severe("Fatal exception occurred while making the access token request: " + line);
        return null;
      }

      // Extract access token
      String accessToken = line.split("&")[0].split("=")[1];
      if (StringUtils.isEmpty(accessToken)) {
        log.severe("Fatal exception occurred while making the access token request: Access token value in response is empty");
        return null;
      }

      return accessToken;
    } catch(Exception e) {
      log.severe("Fatal exception occurred while making the access token request: " + e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        log.severe("Fatal exception occurred while closing HTTP client connection: " + e.getMessage());
        e.printStackTrace();
      }
    }

    return null;
  }

  public static String makeProtectedResourceRequest(String resourceEndpoint, String accessToken) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      httpClient = HttpClients.createDefault();

      // Add authorization header to POST request
      HttpPost httpPost = new HttpPost(resourceEndpoint);
      httpPost.addHeader("Authorization", "Bearer " + accessToken);

      /*
       * Note: The addition of the "method=get" URL-encoded form parameter is necessary for the Facebook Graph APIs.
       *       Other OAuth 2 facebook providers may not require this, and some may even reject it.
       */
      List<NameValuePair> urlParameters = new ArrayList<>();
      urlParameters.add(new BasicNameValuePair("method", "get"));
      httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

      // Make the call
      HttpResponse httpResponse = httpClient.execute(httpPost);

      // Process the response
      String response = "";
      String currentLine;
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      while ((currentLine = bufferedReader.readLine()) != null) {
        response += currentLine;
      }

      return response;
    } catch (IOException e) {
      log.severe("Fatal exception occurred while making the protected resource request: " + e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        log.severe("Fatal exception occurred while closing HTTP client connection: " + e.getMessage());
        e.printStackTrace();
      }
    }

    return null;
  }
}
