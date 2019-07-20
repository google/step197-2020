package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/place/add")
public class AddPlaceFrontendServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    Properties props = ServletUtils.GetProperties();
    String MAPS_API_KEY = props.getProperty("MAPS_API_KEY");
    request.setAttribute("TITLE", "Add Place");
    request.setAttribute(
        "HEAD_HTML",
        "<style>#map { width: 500px; height: 500px; border: thin solid black; }</style>"
            + "<script src=\"https://maps.googleapis.com/maps/api/js?key="
            + MAPS_API_KEY
            + "&libraries=places\"></script>");

    ServletUtils.RenderReact("addPlace", request, response);
  }
}
