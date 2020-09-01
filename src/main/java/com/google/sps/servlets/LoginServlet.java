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
    String urlToRedirect = "/homePage";
    String userId = "null";
    String logoutUrl = "null";
    String loginUrl = "null";
    String userEmail = "null";
    Boolean showNewTab = false;
    String error = null;

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      userEmail = userService.getCurrentUser().getEmail();
      if (userEmail.substring(userEmail.lastIndexOf("@") + 1).equals("google.com")) {
        userId = userService.getCurrentUser().getUserId();
        userEmail = userService.getCurrentUser().getEmail();
        logoutUrl = userService.createLogoutURL(urlToRedirect);
        showNewTab = true;
      } else {
        error = "Unauthorized";
        loginUrl = userService.createLoginURL(urlToRedirect);
      }
    } else {
      loginUrl = userService.createLoginURL(urlToRedirect);
    }
    User user = new User(userId, userEmail);
    if (userId != "null" && !isUserInDatastore(user)) {
      storeUserToDatastore(user);
    }

    Map<String, Object> jsonInfo = new HashMap<>();
    jsonInfo.put("showTabStatus", showNewTab);
    jsonInfo.put("logoutUrl", logoutUrl);
    jsonInfo.put("loginUrl", loginUrl);
    jsonInfo.put("error", error);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonInfo));
  }

  public boolean isUserInDatastore(User user) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key userKey = KeyFactory.createKey("User", user.getUserId());

    try {
      datastore.get(userKey);
      user.setUserKey(KeyFactory.keyToString(userKey));
      return true;
    } catch (EntityNotFoundException e) {
      return false;
    }
  }

  public void storeUserToDatastore(User user) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity newEntity = user.createEntity();
    Key key = newEntity.getKey();
    String userKeyStr = KeyFactory.keyToString(key);
    user.setUserKey(userKeyStr);

    datastore.put(newEntity);
  }
}
