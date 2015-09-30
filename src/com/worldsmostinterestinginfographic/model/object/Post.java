package com.worldsmostinterestinginfographic.model.object;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a Facebook post.
 * 
 * @see https://developers.facebook.com/docs/graph-api/reference/v2.3/post
 */
public final class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private Type type;
	private User from;
	private String message;
	private String statusType;
	private List<User> likes;

	public static enum Type {
		LINK, STATUS, PHOTO, VIDEO, OFFER
	}

	public Post(String id, Type type, User from, String message, String statusType, List<User> likes) {
		this.id = id;
		this.type = type;
		this.from = from;
		this.message = (message == null ? "" : message);
		this.statusType = (statusType == null ? "" : statusType);
		this.likes = likes;
	}

	public String getId() {
		return id;
	}

	public Type getType() {
		return type;
	}
	
	public User getFrom() {
		return from;
	}

	public String getMessage() {
		return message;
	}

	public String getStatusType() {
		return statusType;
	}

	public List<User> getLikes() {
		return likes;
	}
}
