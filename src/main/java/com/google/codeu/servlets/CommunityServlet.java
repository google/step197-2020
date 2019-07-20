package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/community")
public class CommunityServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    Properties props = ServletUtils.GetProperties();
    request.setAttribute("TITLE", "Community");
    request.setAttribute("HEAD_HTML", "<script src=\"/js/navigation-loader.js\"></script>");

    ServletUtils.RenderReact("community", request, response);
  }
}
