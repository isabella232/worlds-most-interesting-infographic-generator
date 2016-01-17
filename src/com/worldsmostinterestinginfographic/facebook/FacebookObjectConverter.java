package com.worldsmostinterestinginfographic.facebook;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.model.object.User;

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
}
