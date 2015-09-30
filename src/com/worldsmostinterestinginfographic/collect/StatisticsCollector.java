package com.worldsmostinterestinginfographic.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		return new TopFriendsResult(likesByFriendsMap);
	}
	
	public static Map<Post.Type, Integer> collectPostTypes(List<Post> posts) {
		Map<Post.Type, Integer> postTypesCount = new HashMap<Post.Type, Integer>();
		
		for (Post post : posts) {
			if (postTypesCount.containsKey(post.getType())) {
				postTypesCount.put(post.getType(), postTypesCount.get(post.getType()) + 1);
				continue;
			}
			
			postTypesCount.put(post.getType(), 1);
		}
		
		return postTypesCount;
	}
}
