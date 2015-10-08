package com.worldsmostinterestinginfographic.servlet;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.LoggingUtils;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CallbackServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(CallbackServlet.class.getName());

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Check for the presence of an authorization code
    String authorizationCode = request.getParameter("code");

    if (!StringUtils.isEmpty(authorizationCode)) {

      // Get access token
      log.info(
          "[" + request.getSession().getId() + "] Starting session.  Requesting access token with authorization code "
          + LoggingUtils.anonymize(authorizationCode));

      String accessToken = requestAccessToken(authorizationCode, request);

      if (StringUtils.isEmpty(accessToken)) {
        response.sendRedirect("/uh-oh");
        return;
      }

      // Get profile data
      log.info("[" + request.getSession().getId() + "] Access token " + LoggingUtils.anonymize(accessToken)
               + " received.  Requesting profile data.");
      
      String userJson = requestProfileData(accessToken);
      User user = convertUserJsonToObject(userJson);

      if (user == null) {
        response.sendRedirect("/uh-oh");
        return;
      }

      // Here we go
      log.info("[" + request.getSession().getId() + "] Hello, " + LoggingUtils.anonymize(Objects.toString(user.getId()))
               + "!");

      Model.cache.put(request.getSession().getId() + ".profile", user);
      Model.cache.put(request.getSession().getId() + ".token", accessToken);

      response.sendRedirect("/you-rock");

    } else if (request.getParameter("error") != null) {

      String error = request.getParameter("error");
      String errorDescription = request.getParameter("error_description");

      // An error happened during authorization code request
      log.severe("Error encountered during authorization code request: " + error + " - " + errorDescription);

      request.getSession().setAttribute("error", error);
      request.getSession().setAttribute("errorDescription", errorDescription);
      response.sendRedirect("/uh-oh");
    } else {
      log.severe("An unknown error encountered at redirection endpoint");
      response.sendRedirect("/uh-oh");
    }
  }

  private String requestAccessToken(String authorizationCode, HttpServletRequest request) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      // Exchange authorization code for access token
      HttpPost httpPost = new HttpPost(
          Model.TOKEN_ENDPOINT + "?grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri="
          + URLEncoder.encode((request.getScheme() + "://" + request.getServerName() + Model.REDIRECTION_ENDPOINT),
                              StandardCharsets.UTF_8.name()) + "&client_id=" + Model.CLIENT_ID + "&client_secret="
          + Model.CLIENT_SECRET);
      HttpResponse httpResponse = httpClient.execute(httpPost);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      String line = bufferedReader.readLine();
      String accessToken = line.split("&")[0].split("=")[1];

      // TODO: Put real error detection here.  Should look for actual error codes and descriptions from the response.
      if (StringUtils.isEmpty(accessToken)) {
        log.severe("[" + request.getSession().getId() + "]An error occurred during access token request");
        return null;
      }

      return accessToken;
    } finally {
      httpClient.close();
    }
  }

  private String requestProfileData(String accessToken) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      // Use access token to request profile data
      String requestUrl = "https://graph.facebook.com/v2.2/me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;
      httpClient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.addHeader("Authorization", "Bearer " + accessToken);
      List<NameValuePair> urlParameters = new ArrayList<>();
      urlParameters.add(new BasicNameValuePair("method", "get"));
      httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
      HttpResponse httpResponse = httpClient.execute(httpPost);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      String userJson = bufferedReader.readLine();
      return userJson;
    } finally {
      httpClient.close();
    }
  }

  private User convertUserJsonToObject(String userJson) {
    User user = null;
    try {
      JSONObject userObject = new JSONObject(userJson);
      user = new User(Long.valueOf(userObject.getString("id")), userObject.getString("name"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return user;
  }
}
