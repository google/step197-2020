package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.google.sps.tool.GoogleTranslationAPI;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;


@WebServlet("/translation")
public class TranslationServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String rawText = request.getParameter("rawText");
      String toLang = request.getParameter("toLang");
      String fromLang = request.getParameter("fromLang");

      String textTranslated = GoogleTranslationAPI.translateText(rawText, toLang);

      // Aggregate information as a json 
      Map<String, String> jsonInfo = new HashMap<>();
      jsonInfo.put("translation", textTranslated);
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonInfo));
    }
  }
}