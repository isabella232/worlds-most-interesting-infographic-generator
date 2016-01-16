package com.worldsmostinterestinginfographic.util;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.User;

public enum FacebookGraphUtils {
  INSTANCE;

  public static User getProfile(String accessToken) {

    // Construct profile API request
    String requestUrl = Model.FACEBOOK_API_ENDPOINT + "me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;

    // Make profile request
    String profileJson = OAuth2Utils.makeProtectedResourceRequest(requestUrl, accessToken);

    // Convert profile JSON to profile object
    User user = new User(profileJson);

    return user;
  }
}
