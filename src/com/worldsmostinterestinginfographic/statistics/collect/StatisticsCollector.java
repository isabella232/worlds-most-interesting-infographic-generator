package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.StatisticsResult;

import java.util.List;

public interface StatisticsCollector {
  public StatisticsResult collect(User user, List<Post> posts);
}
