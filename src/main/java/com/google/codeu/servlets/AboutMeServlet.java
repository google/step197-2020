package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Responds with a hard-coded message for testing purposes. */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

  /** Responds with the "about me" section for a particular user. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    String email = request.getParameter("user");
    if (email.isEmpty() || email.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    User user = User.getByEmail(email);
    if (user == null || user.getAboutMe() == null) {
      return;
    }

    response.getOutputStream().println(user.getAboutMe());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/");
      return;
    }

    String email = userService.getCurrentUser().getEmail();
    String aboutMe = request.getParameter("about-me");
    aboutMe = Jsoup.clean(request.getParameter("about-me"), Whitelist.none());

    User user = User.getByEmail(email);
    user.setAboutMe(aboutMe);
    User.store(user);

    response.sendRedirect("/user-page?user=" + email);
  }
}
