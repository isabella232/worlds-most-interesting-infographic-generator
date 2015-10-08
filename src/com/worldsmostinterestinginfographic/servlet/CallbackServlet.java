package com.worldsmostinterestinginfographic.servlet;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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
}
