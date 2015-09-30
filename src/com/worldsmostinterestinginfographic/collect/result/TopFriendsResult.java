package com.worldsmostinterestinginfographic.collect.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.worldsmostinterestinginfographic.model.object.User;

public class TopFriendsResult {
	
	private List<UserLikeCountPair> topFriends;
	
	public TopFriendsResult(Map<User, Integer> likesByFriends) {
		topFriends = new ArrayList<UserLikeCountPair>();
		for (Map.Entry<User, Integer> entry : likesByFriends.entrySet()) {
			topFriends.add(new UserLikeCountPair(entry.getKey(), entry.getValue()));
		}
		
		Collections.sort(topFriends);
	}

	public List<UserLikeCountPair> getTopFriends() {
		return topFriends;
	}
}
