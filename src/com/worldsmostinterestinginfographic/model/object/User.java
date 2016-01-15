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

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof User)) {
      return false;
    }

    return id == ((User)obj).getId();
  }

  @Override
  public int hashCode() {
    return (int)id;
  }
}
