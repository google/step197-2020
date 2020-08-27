package com.google.sps.servlets;

import com.google.sps.servlets.ServletUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

@WebServlet(
    urlPatterns = {
      "/homePage",
      "/MyFolders",
      "/CreateCard",
      "/CreateFolder",
      "/ImageInterface",
      "/YoutubeInterface",
      "/InsideFolder"
    })
public class ReactServlet2 extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.setAttribute("TITLE", "Frame.cards");
    ServletUtils.RenderReact("mainApp", request, response);
  }
}
