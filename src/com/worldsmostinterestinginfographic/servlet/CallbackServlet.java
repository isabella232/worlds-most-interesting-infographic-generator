package com.worldsmostinterestinginfographic.servlet;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.util.LoggingUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

      log.info(
          "[" + request.getSession().getId() + "] Starting session.  Requesting access token with authorization code "
          + LoggingUtils.anonymize(authorizationCode));
      String accessToken = requestAccessToken(authorizationCode, request);

      if (StringUtils.isEmpty(accessToken)) {
        response.sendRedirect("/uh-oh");
      }

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

    response.sendRedirect("/you-rock");
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
        log.severe("An error occurred during access token request");
        return null;
      }

      return accessToken;
    } finally {
      httpClient.close();
    }
  }
}
