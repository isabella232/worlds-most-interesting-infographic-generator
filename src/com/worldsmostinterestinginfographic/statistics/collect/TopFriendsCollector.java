package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.TopFriendsResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TopFriendsCollector implements StatisticsCollector {

  @Override
  public TopFriendsResult collect(User user, List<Post> posts) {

    // Populate friends-likes map
    Map<User, Integer> friendsLikesMap = new HashMap<User, Integer>();
    for (Post post : posts) {
      for (User liker : post.getLikes()) {

        // Ignore own likes
        if (liker.equals(user)) {
          continue;
        }

        if (!friendsLikesMap.containsKey(liker)) {
          friendsLikesMap.put(liker, 1);
          continue;
        }

        friendsLikesMap.put(liker, friendsLikesMap.get(liker) + 1);
      }
    }

    // Convert friends-likes map from map to sorted list ordered by like count (i.e. index 0 has friend with most likes)
    List<Map.Entry<User, Integer>>  topFriendsList = new LinkedList<Map.Entry<User, Integer>>(friendsLikesMap.entrySet());
    Collections.sort(topFriendsList, new Comparator<Map.Entry<User,Integer>>() {
      public int compare(Map.Entry<User, Integer> o1,
                         Map.Entry<User, Integer> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    return new TopFriendsResult(topFriendsList);
  }
}
