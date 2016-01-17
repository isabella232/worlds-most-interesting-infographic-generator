package com.worldsmostinterestinginfographic.facebook;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.OAuth2Utils;

import java.util.List;

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

  public List<Post> getFeedPosts(String accessToken) {

    // Construct feed API request
    String requestUrl = Model.FACEBOOK_API_ENDPOINT + "me/feed?limit=" + Model.MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST +
                        "&access_token=" + accessToken + "&fields=" + Model.FACEBOOK_REQUESTED_FEED_FIELDS;

    // Make feed request
    String postsJson = OAuth2Utils.makeProtectedResourceRequest(requestUrl, accessToken);

    // Convert posts JSON to posts list
    List<Post> posts = facebookObjectConverter.convertJsonToPosts(postsJson);

    return posts;
  }
}
