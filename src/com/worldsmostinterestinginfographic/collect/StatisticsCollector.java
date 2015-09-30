package com.worldsmostinterestinginfographic.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.worldsmostinterestinginfographic.collect.result.TopFriendsResult;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;

public class StatisticsCollector {
	public static TopFriendsResult collectTopFriends(List<Post> posts, User user) {
		
		
		System.out.println("# of posts: " + posts.size());
		
		Map<User, Integer> likesByFriendsMap = new HashMap<User, Integer>();
		for (Post post : posts) {
			
			for (User liker : post.getLikes()) {
				
				// Ignore own likes
				if (liker.equals(user)) {
					continue;
				}
				
				if (likesByFriendsMap.containsKey(liker)) {
					likesByFriendsMap.put(liker, likesByFriendsMap.get(liker) + 1);
					continue;
				}
				
				likesByFriendsMap.put(liker, 1);
			}
			
		}
		
		SortedMap<Integer, User> likersByCount = new TreeMap<Integer, User>();
		for (Map.Entry<User, Integer> entry : likesByFriendsMap.entrySet()) {
			
			// Ignore own likes
			if (entry.getKey().equals(user)) {
				continue;
			}
			
			likersByCount.put(entry.getValue(), entry.getKey());
//			System.out.println(entry.getKey().getName() + " --> " + entry.getValue());
		}
		
		return new TopFriendsResult(likesByFriendsMap);
//		return null;
	}
}
