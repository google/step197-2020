package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    Properties props = ServletUtils.GetProperties();
    request.setAttribute("TITLE", "Message Feed");
    request.setAttribute(
        "HEAD_HTML", "<link rel='stylesheet' href='/css/main.css'>"
                         + "<link rel='stylesheet' href='/css/user-page.css'>");

    ServletUtils.RenderReact("feed", request, response);
  }
}
