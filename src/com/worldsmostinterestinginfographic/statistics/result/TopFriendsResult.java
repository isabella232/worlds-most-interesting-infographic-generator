package com.worldsmostinterestinginfographic.statistics.result;

import com.whoischarles.util.json.Minify;
import com.worldsmostinterestinginfographic.model.object.User;

import java.util.List;
import java.util.Map;

public class TopFriendsResult implements StatisticsResult, InfographicResult {

  private List<Map.Entry<User,Integer>> topFriends;
  private String error;

  public TopFriendsResult(List<Map.Entry<User,Integer>> topFriends) {
    if (topFriends == null) {
      throw new IllegalArgumentException();
    }

    this.topFriends = topFriends;
  }

  public TopFriendsResult(String error) {
    this.error = error;
  }

  @Override
  public List<Map.Entry<User,Integer>> getResult() {
    return topFriends;
  }

  @Override
  public String getError() {
    return error;
  }

  @Override
  public String getInfographicJson() {

    if (topFriends.size() < 4) {
      return null;
    }

    String json = "{" +
                  "  \"friends\": [" +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(0).getKey().getId() + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(0).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(0).getKey().getName() + "\"," +
                  "      \"color\": \"#3b5998\"" +
                  "    }," +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(1).getKey().getId() + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(1).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(1).getKey().getName() + "\"," +
                  "      \"color\": \"#5bc0bd\"" +
                  "    }," +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(2).getKey().getId() + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(2).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(2).getKey().getName() + "\"," +
                  "      \"color\": \"#f08a4b\"" +
                  "    }," +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(3).getKey().getId() + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(3).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(3).getKey().getName() + "\"," +
                  "      \"color\": \"#1c2541\"" +
                  "    }" +
                  "  ]" +
                  "}";

    return new Minify().minify(json);
  }
}
