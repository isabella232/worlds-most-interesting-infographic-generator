package com.worldsmostinterestinginfographic.statistics.result;

import com.whoischarles.util.json.Minify;
import com.worldsmostinterestinginfographic.model.object.Post;

import java.util.Map;

public class PostTypesResult implements StatisticsResult, InfographicResult {

  private Map<Post.Type, Integer> postTypesCount;
  private String error;

  public PostTypesResult(Map<Post.Type, Integer> postTypesCount) {
    if (postTypesCount == null) {
      throw new IllegalArgumentException();
    }

    this.postTypesCount = postTypesCount;
  }

  public PostTypesResult(String error) {
    this.error = error;
  }

  @Override
  public Object getResult() {
    return postTypesCount;
  }

  @Override
  public String getError() {
    return error;
  }

  @Override
  public String getInfographicJson() {
    String json = "{" +
                  "  \"types\": [" +
                  "    {" +
                  "      \"type\": \"" + (postTypesCount.containsKey(Post.Type.STATUS) ? postTypesCount.get(Post.Type.STATUS) : 0) + "\"," +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.STATUS) ? postTypesCount.get(Post.Type.STATUS) : 0) + "," +
                  "      \"description\": \"Status Update\"," +
                  "      \"color\": \"#3b5998\"" +
                  "    }," +
                  "    {" +
                  "      \"type\": \"" + (postTypesCount.containsKey(Post.Type.PHOTO) ? postTypesCount.get(Post.Type.PHOTO) : 0) + "\"," +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.PHOTO) ? postTypesCount.get(Post.Type.PHOTO) : 0) + "," +
                  "      \"description\": \"Image Post\"," +
                  "      \"color\": \"#5bc0bd\"" +
                  "    }," +
                  "    {" +
                  "      \"type\": \"" + (postTypesCount.containsKey(Post.Type.LINK) ? postTypesCount.get(Post.Type.LINK) : 0) + "\"," +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.LINK) ? postTypesCount.get(Post.Type.LINK) : 0) + "," +
                  "      \"description\": \"Shared Link\"," +
                  "      \"color\": \"#2ebaeb\"" +
                  "    }," +
                  "    {" +
                  "      \"type\": \"" + (postTypesCount.containsKey(Post.Type.VIDEO) ? postTypesCount.get(Post.Type.VIDEO) : 0) + "\"," +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.VIDEO) ? postTypesCount.get(Post.Type.VIDEO) : 0) + "," +
                  "      \"description\": \"Video Post\"," +
                  "      \"color\": \"#f08a4b\"" +
                  "    }" +
                  "  ]" +
                  "}";

    return new Minify().minify(json);
  }
}
