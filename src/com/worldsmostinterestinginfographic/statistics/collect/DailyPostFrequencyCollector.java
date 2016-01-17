package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.DailyPostFrequencyResult;

import java.util.Calendar;
import java.util.List;

public class DailyPostFrequencyCollector implements StatisticsCollector {

  @Override
  public DailyPostFrequencyResult collect(User user, List<Post> posts) {
    int[] postsByDayOfWeek = new int[7];
    for (Post post : posts) {
      if (post.getCreatedDate() != null) {
        postsByDayOfWeek[post.getCreatedDate().get(Calendar.DAY_OF_WEEK) - 1] += 1;
      }
    }

    return new DailyPostFrequencyResult(postsByDayOfWeek);
  }
}
