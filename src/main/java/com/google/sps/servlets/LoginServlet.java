package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    String urlToRedirect = "/";
    String userId = "null";
    String logoutUrl = "null";
    String loginUrl = "null";
    String userEmail = "null";

    // Boolean to update User's main page 
    Boolean showNewTab = false;

    User user;
    if (userService.isUserLoggedIn()) {
      userId = userService.getCurrentUser().getUserId();
      userEmail = userService.getCurrentUser().getEmail();
      logoutUrl = userService.createLogoutURL(urlToRedirect);
      showNewTab = true;
      user = new User(userId, userEmail);
    } else {
      loginUrl = userService.createLoginURL(urlToRedirect);
      user = new User(userId, userEmail);
    }

    if (userId != "null") {
      if (!isUserInDatastore(user)) {
        storeUserToDatastore(user);
      }
    }

    // Aggregate response information
    Map<String, Object> jsonInfo = new HashMap<>();
    jsonInfo.put("userInfo", user);
    jsonInfo.put("showTabStatus", showNewTab);
    jsonInfo.put("logoutUrl", logoutUrl);
    jsonInfo.put("loginUrl", loginUrl);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonInfo));
  }

  public boolean isUserInDatastore(User user) {
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key userKey = KeyFactory.createKey("User", user.getUserId());
    Entity userEntity;

    // Check if user is in datastore
    try {
      userEntity = datastore.get(userKey);
      user.setUserKey(KeyFactory.keyToString(userKey));
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
  }

  public void storeUserToDatastore(User user) {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Create new user entity 
    Entity newEntity = user.createEntity();

    // Create User's key and add to object
    Key key = newEntity.getKey(); 
    String userKeyStr = KeyFactory.keyToString(key);
    user.setUserKey(userKeyStr);

    datastore.put(newEntity);
  }

}