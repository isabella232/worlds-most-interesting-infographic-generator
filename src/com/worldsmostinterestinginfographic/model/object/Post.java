package com.worldsmostinterestinginfographic.model.object;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public final class Post implements Serializable {

  private String id;
  private Type type;
  private User from;
  private String message;
  private String statusType;
  private List<User> likes;
  private Calendar createdDate;

  public static enum Type {
    LINK, STATUS, PHOTO, VIDEO, OFFER, EVENT
  }

  public Post(String id, Type type, User from, String message, String statusType, List<User> likes, Calendar createdDate) {
    this.id = id;
    this.type = type;
    this.from = from;
    this.message = (message == null ? "" : message);
    this.statusType = (statusType == null ? "" : statusType);
    this.likes = likes;
    this.createdDate = createdDate;
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

  public Calendar getCreatedDate() {
    return createdDate;
  }

}
