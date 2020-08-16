package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public final class User {
  private final String userId;
  private String email;
  private String userKey;

  public User(String userId, String email) {
    this.userId = userId;
    this.email = email;
    this.userKey = "null";
  }

  public String getUserId() {
    return this.userId;
  }

  public String getEmail() {
    return this.email;
  }

  public String getUserKey() {
    return this.userKey;
  }

  public void setUserKey(String userKey) {
    this.userKey = userKey;
  }

  public Entity createEntity() {
    Entity user = new Entity("User", this.userId);
    user.setProperty("email", this.email);
    return user;
  }
}
