package com.worldsmostinterestinginfographic.servlet;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.facebook.FacebookService;
import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.collect.StatisticsCollector;
import com.worldsmostinterestinginfographic.statistics.collect.TopFriendsCollector;
import com.worldsmostinterestinginfographic.statistics.result.InfographicResult;
import com.worldsmostinterestinginfographic.statistics.result.StatisticsResult;
import com.worldsmostinterestinginfographic.util.LoggingUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatisticsServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(StatisticsServlet.class.getName());

  private final FacebookService facebookService;

  public StatisticsServlet() {
    facebookService = new FacebookService();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    User user = (User) Model.cache.get(request.getSession().getId() + ".profile");
    if (user == null) {
      log.severe("[" + request.getSession().getId() + "] Invalid session, no profile found in cache");
    }

    String accessToken = Objects.toString(Model.cache.get(request.getSession().getId() + ".token"));
    if (accessToken == null) {
      log.severe("[" + request.getSession().getId() + "] Invalid session, no token found in cache");
    }

    // Get feed data
    log.info("[" + request.getSession().getId() + "] Access token " + LoggingUtils.anonymize(accessToken)
             + ".  Requesting feed data.");

    List<Post> posts = facebookService.getFeedPosts(accessToken);
    if (posts.size() <= 0) {
      response.getWriter().println("[]");
      return;
    }

    log.info("[" + request.getSession().getId() + "] Received " + posts.size() + " stories for user " +
             LoggingUtils.anonymize(Objects.toString(user.getId())) + ". Collecting statistics...");

    // Collect statistics
    StatisticsCollector topFriendsCollector = new TopFriendsCollector();

    StatisticsResult topFriendsResult = topFriendsCollector.collect(user, posts);

    // Convert statistics objects to JSON response strings
    String topFriendsJson = ((InfographicResult) topFriendsResult).getInfographicJson();

    String result = "";
    try {
      JSONObject topFriendsObject = new JSONObject(topFriendsJson);

      JSONObject resultObject = new JSONObject();
      resultObject.put("TOP_FRIENDS", topFriendsObject);

      result = resultObject.toString();
    } catch (JSONException e) {
      log.severe("[" + request.getSession().getId() + "] Error encountered while constructing response JSON: " +
                 e.getMessage());
      e.printStackTrace();
    }

    response.getWriter().println(result);
  }
}
