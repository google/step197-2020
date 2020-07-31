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

    if (userService.isUserLoggedIn()) {
      userEmail = userService.getCurrentUser().getEmail();
      userId = userService.getCurrentUser().getUserId();
      logoutUrl = userService.createLogoutURL(urlToRedirect);
      showNewTab = true;
    } else {
      loginUrl = userService.createLoginURL(urlToRedirect);
    }

    User user = new User(userId, loginUrl, logoutUrl, userEmail, showNewTab);
    
    if (userId != "null") {
        checkUserInDatastore(user);
    }

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(user));
  }

  public void checkUserInDatastore(User user) {
    // ** NEED TO WRITE TEST FOR BEHAVIORAL TESTING

    Key userKey = KeyFactory.createKey("User", user.getUserId());
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity;

    // Check if user is in datastore
    try {
      userEntity = datastore.get(userKey);
      user.setUserKey(KeyFactory.keyToString(userKey));
    } catch (EntityNotFoundException e) {
      userEntity = null;
    }

    if (userEntity == null) {
        addUserToDatastore(datastore, user);
    }
  }

  public void addUserToDatastore(DatastoreService datastore, User user) {

    // Create new user entity 
    Entity newEntity = user.createEntity();

    // Create User's key and add to object
    Key key = newEntity.getKey(); 
    String userKeyStr = KeyFactory.keyToString(key);
    user.setUserKey(userKeyStr);

    datastore.put(newEntity);
  }

}