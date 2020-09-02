package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.servlets.ServletUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

@WebServlet(
    urlPatterns = {
      "",
      "/homePage",
      "/MyFolders",
      "/CreateCard",
      "/CreateFolder",
      "/ImageInterface",
      "/StudyMode",
      "/InsideStudyMode",
      "/SimilarWords",
      "/InsideFolder"
    })
public class ReactServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    UserService userService = UserServiceFactory.getUserService();
    String servletPath = request.getServletPath();
    if (servletPath.equals("/homePage")) {
      request.setAttribute("TITLE", "Home");
      ServletUtils.RenderReact("homePage", request, response);
    } else if (servletPath.equals("")) {
      response.sendRedirect("/homePage");
    } else if (userService.isUserLoggedIn()) {
      request.setAttribute("TITLE", "Frame.cards");
      ServletUtils.RenderReact("mainApp", request, response);
    } else {
      response.sendRedirect("/homePage");
    }
  }
}
