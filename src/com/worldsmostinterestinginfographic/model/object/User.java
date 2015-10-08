package com.worldsmostinterestinginfographic.model.object;

import java.io.Serializable;

public final class User implements Serializable {

  private long id;
  private String name;

  public User(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
