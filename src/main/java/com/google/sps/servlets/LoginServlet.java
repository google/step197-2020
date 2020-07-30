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

    // Boolean to update User's main page 
    String showNewTab = false;

    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String userId = userService.getCurrentUser().getUserId();
      String logoutUrl = userService.createLogoutURL(urlToRedirect);
      String showNewTab = true;
    } else {
      String loginUrl = userService.createLoginURL(urlToRedirect);
    }

    User user = new User(userId, loginUrl, logoutUrl, email, showNewTab);
    
    checkUserInDatastore(user);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(user));
  }

  public void checkUserInDatastore(User user) {
    // ** NEED TO WRITE TEST FOR BEHAVIORAL TESTING

    Key userKey = KeyFactory.createKey("User", user.getUserId());
    if (datastore.get(userkey) == null) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Create new user entity 
        Entity userEntity = user.createEntity();
        Key userKey = userEntity.getKey();

        // Create User's key and add to object 
        String userKeyStr = KeyFactory.keyToString(userKey);
        user.setUserKey(userKeyStr);

        datastore.put(userEntity);
    }
  }
}