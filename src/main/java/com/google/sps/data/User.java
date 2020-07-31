package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public final class User {
    private final String userId;
    private String loginUrl;
    private String logoutUrl;
    private String email;
    private boolean showNewTab;
    private String userKey;
      
    public User(String userId, String loginUrl, String logoutUrl, String email, boolean showNewTab) {
      this.userId = userId;
      this.loginUrl = loginUrl;
      this.logoutUrl = logoutUrl;
      this.email = email;
      this.showNewTab = showNewTab;
      this.userKey = "null";
    }

    public String getUserId() {
        return this.userId;
    }
    
    public String getLoginUrl() {
        return this.loginUrl;
    }

    public String getLogoutUrl() {
        return this.logoutUrl;
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
        // Create user entity identified by userId
        Entity user = new Entity("User", this.userId);
        user.setProperty("email", this.email);
        return user;
    }
}

