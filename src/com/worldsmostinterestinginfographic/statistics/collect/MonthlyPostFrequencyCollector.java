package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.MonthlyPostFrequencyResult;

import java.util.Calendar;
import java.util.List;

public class MonthlyPostFrequencyCollector implements StatisticsCollector {

  @Override
  public MonthlyPostFrequencyResult collect(User user, List<Post> posts) {
    int[] postsByMonthOfYear = new int[12];
    for (Post post : posts) {
      if (post.getCreatedDate() != null) {
        postsByMonthOfYear[post.getCreatedDate().get(Calendar.MONTH)] += 1;
      }
    }

    return new MonthlyPostFrequencyResult(postsByMonthOfYear);
  }
}
