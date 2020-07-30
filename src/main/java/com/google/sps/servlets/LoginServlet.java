package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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
    String jsonInfo;
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String logoutUrl = userService.createLogoutURL(urlToRedirect);
      jsonInfo = convertToJson("null", logoutUrl, userEmail, true);

    } else {
      String loginUrl = userService.createLoginURL(urlToRedirect);
      jsonInfo = convertToJson(loginUrl, "null", "null", false);
    }

    response.setContentType("application/json;");
    response.getWriter().println(jsonInfo);
  }

  private String convertToJson(String loginUrl, String logoutUrl, String email, boolean showForm) {
    Gson gson = new Gson();
    User user = new User(loginUrl, logoutUrl, email, showForm);
    String json = gson.toJson(user);
    return json;
  }
}