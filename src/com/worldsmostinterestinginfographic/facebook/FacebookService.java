package com.worldsmostinterestinginfographic.facebook;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.OAuth2Utils;

public class FacebookService {

  private final FacebookObjectConverter facebookObjectConverter;

  public FacebookService() {
    facebookObjectConverter = new FacebookObjectConverter();
  }

  public User getProfile(String accessToken) {

    // Construct profile API request
    String requestUrl = Model.FACEBOOK_API_ENDPOINT + "me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;

    // Make profile request
    String profileJson = OAuth2Utils.makeProtectedResourceRequest(requestUrl, accessToken);

    // Convert profile JSON to profile object
    User user = facebookObjectConverter.convertJsonToUser(profileJson);

    return user;
  }
}
