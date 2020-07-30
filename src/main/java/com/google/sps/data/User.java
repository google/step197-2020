package com.google.sps.data;

public final class User {
    private String loginUrl;
    private String logoutUrl;
    private String email;
    private boolean showForm;
      
    public User(String loginUrl, String logoutUrl, String email, boolean showForm) {
      this.loginUrl = loginUrl;
      this.logoutUrl = logoutUrl;
      this.email = email;
      this.showForm = showForm;
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
}

