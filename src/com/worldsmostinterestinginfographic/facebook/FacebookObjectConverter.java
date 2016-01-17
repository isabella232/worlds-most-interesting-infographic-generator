package com.worldsmostinterestinginfographic.facebook;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacebookObjectConverter {
  public User convertJsonToUser(String userJson) {
    User user = null;
    try {
      JSONObject userObject = new JSONObject(userJson);
      user = new User(Long.valueOf(userObject.getString("id")), userObject.getString("name"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return user;
  }

  public List<Post> convertJsonToPosts(String postsJson) {
    List<Post> posts = new ArrayList<>();
    try {
      JSONObject postsObject = new JSONObject(postsJson);
      JSONArray postsArray = new JSONArray(postsObject.getString("data"));

      for (int i = 0; i < postsArray.length(); i++) {
        Post post = convertJsonToPost(postsArray.get(i).toString());
        if (post != null) {
          posts.add(post);
        }
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return posts;
  }

  public Post convertJsonToPost(String postJson) {
    Post post = null;

    if (postJson == null) {
      return null;
    }

    try {
      JSONObject postObject = new JSONObject(postJson);

      // Get id
      String id = postObject.getString("id");

      // Get type
      Post.Type type = Post.Type.valueOf(postObject.getString("type").toUpperCase());

      // Get message
      String message = postObject.has("message") ? postObject.getString("message") : "";

      // Get status type
      String statusType = postObject.has("status_type") ? postObject.getString("status_type") : null;

      // Get created date
      String createdDateString = postObject.has("created_time") ? postObject.getString("created_time") : null;
      Calendar createdDate = null;
      if (createdDateString != null) {
        createdDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
          createdDate.setTime(sdf.parse(createdDateString));// all done
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }

      // Get from
      User from = null;
      boolean hasFrom = postObject.has("from");
      if (hasFrom) {
        JSONObject fromObject = postObject.getJSONObject("from");
        from = new User(Long.valueOf(fromObject.getString("id")), fromObject.getString("name"));
      }

      // Get likes
      List<User> likes = new ArrayList<User>();
      boolean hasLikes = postObject.has("likes");
      if (hasLikes) {
        JSONArray likesArray = postObject.getJSONObject("likes").getJSONArray("data");
        for (int i = 0; i < likesArray.length(); i++) {
          JSONObject likerObject = (JSONObject)likesArray.get(i);
          User liker = new User(Long.valueOf(likerObject.getString("id")), likerObject.getString("name"));
          likes.add(liker);
        }
      }

      post = new Post(id, type, from, message, statusType, likes, createdDate);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return post;
  }
}
