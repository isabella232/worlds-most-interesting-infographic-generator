package com.worldsmostinterestinginfographic.servlet;

import com.worldsmostinterestinginfographic.facebook.FacebookService;
import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.LoggingUtils;
import com.worldsmostinterestinginfographic.util.OAuth2Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CallbackServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(CallbackServlet.class.getName());

  private final FacebookService facebookService;

  public CallbackServlet() {
    facebookService = new FacebookService();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Check for the presence of an authorization code
    String authorizationCode = request.getParameter("code");
    if (!StringUtils.isEmpty(authorizationCode)) {

      // Get access token
      log.info(
          "[" + request.getSession().getId() + "] Starting session.  Requesting access token with authorization code "
          + LoggingUtils.anonymize(authorizationCode));

      String tokenEndpoint = Model.TOKEN_ENDPOINT + "?grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri="
                             + URLEncoder.encode((request.getScheme() + "://" + request.getServerName() + Model.REDIRECTION_ENDPOINT),
                                                 StandardCharsets.UTF_8.name()) + "&client_id=" + Model.CLIENT_ID + "&client_secret="
                             + Model.CLIENT_SECRET;
      String accessToken = OAuth2Utils.requestAccessToken(tokenEndpoint);
      if (StringUtils.isEmpty(accessToken)) {
        response.sendRedirect("/uh-oh");
        return;
      }

      // Get profile data
      log.info("[" + request.getSession().getId() + "] Access token " + LoggingUtils.anonymize(accessToken)
               + " received.  Requesting profile data.");

      User user = facebookService.getProfile(accessToken);
      if (user == null) {
        response.sendRedirect("/uh-oh");
        return;
      }

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

      log.severe("[" + request.getSession().getId() + "] Error encountered during authorization code request: " +
                 error + " - " + errorDescription);
      response.sendRedirect("/uh-oh");

    } else {
      log.severe("[" + request.getSession().getId() + "] An unknown error encountered at redirection endpoint");
      response.sendRedirect("/uh-oh");
    }
  }
}
