package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class HomeServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    Properties props = ServletUtils.GetProperties();

    request.setAttribute("MAPS_API_KEY", props.getProperty("MAPS_API_KEY"));

    request.getRequestDispatcher("/WEB-INF/index.jsp")
        .forward(request, response);
  }
}
