package com.google.sps.servlets;

import com.google.sps.servlets.ServletUtils;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

@WebServlet("/app")
public class ReactServlet extends HttpServlet {
  
  //If User does not exist in database, we create a new User entity. Page redirected to the /MyFolder Url
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
      ServletUtils.RenderReact("index", request, response);
      
  }

}