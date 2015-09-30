package com.worldsmostinterestinginfographic.collect.result;

import com.worldsmostinterestinginfographic.model.object.User;

public class UserLikeCountPair implements Comparable<UserLikeCountPair> {
	private User user;
	private int count;
	
	public UserLikeCountPair(User user, int count) {
		this.user = user;
		this.count = count;
	}
	
	@Override
	public int compareTo(UserLikeCountPair o) {
		return count < o.count ? 1 : count > o.count ? -1 : 0;
	}

	public User getUser() {
		return user;
	}

	public int getCount() {
		return count;
	}
}
