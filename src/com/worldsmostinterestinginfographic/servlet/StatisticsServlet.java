package com.worldsmostinterestinginfographic.servlet;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.LoggingUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatisticsServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(StatisticsServlet.class.getName());

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    User user = (User)Model.cache.get(request.getSession().getId() + ".profile");

    if (user == null) {
      // TODO: Handle this
    }

    String accessToken = Objects.toString(Model.cache.get(request.getSession().getId() + ".token"));

    if (accessToken == null) {
      // TODO: Handle this
    }

    String postsJson = requestFeedData(accessToken);

    List<Post> posts = convertPostsJsonToObject(postsJson);

    // TODO: Make this graceful exit more...graceful
    if (posts.size() <= 0) {
      out.println("[]");
      return;
    }

    log.info("[" + request.getSession().getId() + "] Received " + posts.size() + " stories for user " +
             LoggingUtils.anonymize(Objects.toString(user.getId())) + ". Collecting statistics...");

    out.println(postsJson);
  }

  private String requestFeedData(String accessToken) throws IOException {
    // TODO: Switch to using Authentication Header method for passing access token
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      // Use access token to request posts
      // TODO: URL-encode "likes{id,name}" properly
      String requestUrl = "https://graph.facebook.com/v2.5/me/feed?limit=" + Model.MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST + "&access_token=" + accessToken +
          "&fields=id,name,type,message,status_type,created_time,from,likes%7Bid,name%7D";
      httpClient = HttpClients.createDefault();
      HttpGet get = new HttpGet(requestUrl);
      HttpResponse httpResponse = httpClient.execute(get);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

      String allPostsJson = bufferedReader.readLine();
      return allPostsJson;
    } finally {
      httpClient.close();
    }
  }

  private List<Post> convertPostsJsonToObject(String postsJson) {
    List<Post> posts = new ArrayList<>();
    try {
      JSONObject allPostsObject = new JSONObject(postsJson);
      JSONArray allPostsArray = new JSONArray(allPostsObject.getString("data"));

      for (int i = 0; i < allPostsArray.length(); i++) {
//System.out.println("Adding post " + i);
        JSONObject postObject = (JSONObject)allPostsArray.get(i);
        Post post = parseJsonPost(postObject);
        posts.add(post);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return posts;
  }

  private Post parseJsonPost(JSONObject jsonPost) {
//		System.out.println(jsonPost);
    if (jsonPost == null) {
      return null;
    }

    Post post = null;
    try {

      String id = jsonPost.getString("id");
      Post.Type type = Post.Type.valueOf(jsonPost.getString("type").toUpperCase());
//			User from = new User(jsonPost.getJSONObject("from"))
      String message = jsonPost.has("message") ? jsonPost.getString("message") : "";
      String statusType = jsonPost.has("status_type") ? jsonPost.getString("status_type") : null;
      String createdDateString = jsonPost.has("created_time") ? jsonPost.getString("created_time") : null;



      // get the date using Java Date
			/*
			SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			Date createdDate = null;
			try {
				createdDate = incomingFormat.parse(createdDateString);
				System.out.println(createdDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}*/

      Calendar createdDate = null;
      if (createdDateString != null) {
        createdDate = Calendar.getInstance();
        // get the date using Java Calendar
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
//					if (createdDateString == null) {
//						System.out.println("asdf");
//					}
//					System.out.println(createdDateString);
          createdDate.setTime(sdf.parse(createdDateString));// all done
//					System.out.println(new Date(createdDate.getTimeInMillis()));
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }

//			log.info(jsonPost.getJSONObject("from").toString());


      // playing around with getting FROM
      User from = null;
      boolean hasFrom = jsonPost.has("from");
      if (hasFrom) {
//				log.info("Has from");
//				String fromString = jsonPost.getString("from");
//				JSONArray fromArray = jsonPost.getJSONArray("from");
//				log.info("XXX: " + fromString);
        JSONObject fromObject = jsonPost.getJSONObject("from");

//				System.out.println("String: " + fromObject.getString("id") + "\tLong: " + fromObject.getLong("id"));

        from = new User(Long.valueOf(fromObject.getString("id")), fromObject.getString("name"));
//				log.info("YYY: " + fromObject.getString("name"));
      } else {
        log.info("No from");
      }
//			JSONArray fromArray = jsonPost.has("from") ? jsonPost.getJSONArray("from") : null;
//			log.info(fromArray.toString());


      List<User> likes = new ArrayList<User>();
      boolean hasLikes = jsonPost.has("likes");
      if (hasLikes) {
//				log.info("Has from");
//				String fromString = jsonPost.getString("from");
//				JSONArray fromArray = jsonPost.getJSONArray("from");
//				log.info("XXX: " + fromString);
        JSONArray likesArray = jsonPost.getJSONObject("likes").getJSONArray("data");
        for (int i = 0; i < likesArray.length(); i++) {
          JSONObject likerObject = (JSONObject)likesArray.get(i);
          User liker = new User(Long.valueOf(likerObject.getString("id")), likerObject.getString("name"));
          likes.add(liker);
        }
//				from = new User(fromObject.getLong("id"), fromObject.getString("name"));
//				log.info("YYY: " + fromObject.getString("name"));
      } else {
//				log.info("No likes");
      }


      post = new Post(id, type, from, message, statusType, likes, createdDate);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return post;
  }
}
