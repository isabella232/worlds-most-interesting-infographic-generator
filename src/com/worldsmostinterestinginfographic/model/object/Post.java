package com.worldsmostinterestinginfographic.model.object;

import java.io.Serializable;

/**
 * This class represents a Facebook post.
 * 
 * @see https://developers.facebook.com/docs/graph-api/reference/v2.3/post
 */
public final class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private Type type;
	private String message;
	private String statusType;

	public static enum Type {
		LINK, STATUS, PHOTO, VIDEO, OFFER
	}

	public Post(String id, Type type, String message, String statusType) {
		this.id = id;
		this.type = type;
		this.message = (message == null ? "" : message);
		this.statusType = (statusType == null ? "" : statusType);
	}

	public String getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public String getStatusType() {
		return statusType;
	}
}
