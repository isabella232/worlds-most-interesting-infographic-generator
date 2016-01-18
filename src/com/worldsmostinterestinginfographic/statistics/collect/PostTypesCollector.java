package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.PostTypesResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostTypesCollector implements StatisticsCollector {

  @Override
  public PostTypesResult collect(User user, List<Post> posts) {

    // Populate post-types map
    Map<Post.Type, Integer> postTypesCount = new HashMap<>();
    for (Post post : posts) {
      if (!postTypesCount.containsKey(post.getType())) {
        postTypesCount.put(post.getType(), 1);
        continue;
      }

      postTypesCount.put(post.getType(), postTypesCount.get(post.getType()) + 1);
    }

    return new PostTypesResult(postTypesCount);
  }
}
