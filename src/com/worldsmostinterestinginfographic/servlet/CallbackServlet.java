package com.worldsmostinterestinginfographic.servlet;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.service.FacebookService;
import com.worldsmostinterestinginfographic.service.ServiceProvider;
import com.worldsmostinterestinginfographic.util.FacebookGraphUtils;
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

@Singleton
public class CallbackServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(CallbackServlet.class.getName());

  @Inject
  private ServiceProvider facebookService;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String blah = facebookService.sayHello();

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

      User user = FacebookGraphUtils.getProfile(accessToken);
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

      // An error happened during authorization code request
      String error = request.getParameter("error");
      String errorDescription = request.getParameter("error_description");

      request.getSession().setAttribute("error", error);
      request.getSession().setAttribute("errorDescription", errorDescription);

      log.severe("Error encountered during authorization code request: " + error + " - " + errorDescription);
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
        log.severe("[" + request.getSession().getId() + "] An error occurred during access token request");
        return null;
      }

      return accessToken;
    } finally {
      httpClient.close();
    }
  }

  /**
   * Use access token to request profile data.
   *
   * The access token is of type <code>bearer</code> and so we are using the most secure method for passing access
   * tokens: the authorization request header field method.  Using this method, we attach an additional
   * <code>Authorization</code> header with its value being the token type, in our case <code>Bearer</code>, followed by
   * our token value.  An example request using the authorization request header field would look like...
   *
   *    GET /resource HTTP/1.1
   *    Host: server.example.com
   *    Authorization: Bearer mF_9.B5f-4.1JqM
   *
   * Response will contain profile data for user in JSON format.
   *
   * @param accessToken A valid access token
   * @return Profile data for the user in JSON format
   */
  private String requestProfileData(String accessToken) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      httpClient = HttpClients.createDefault();

      // Construct profile API request
      String requestUrl = Model.FACEBOOK_API_ENDPOINT + "me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;
//  requestUrl = "https://www.googleapis.com/tasks/v1/users/@me/lists/";
//  accessToken = "ya29.awKuvyg42XFkMlGsPmhqG8HFtn6UlhzohwV_AH-R-QKSgyew28aRHE5HScit7Luc8O7Q";

      // Add authorization header to POST request
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.addHeader("Authorization", "Bearer " + accessToken);
      List<NameValuePair> urlParameters = new ArrayList<>();
      urlParameters.add(new BasicNameValuePair("method", "get"));
      httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

      // Make the call
      HttpResponse httpResponse = httpClient.execute(httpPost);

      // Process the response
      String userJson = "";
      String currentLine;
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      while ((currentLine = bufferedReader.readLine()) != null) {
        userJson += currentLine;
      }

      return userJson;
    } finally {
      httpClient.close();
    }
  }

  // TODO: May be able to remove those helper method.  Perhaps can put as constructor in User class, or utility class.
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
