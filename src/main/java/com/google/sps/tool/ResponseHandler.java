package com.google.sps.tool;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;

public class ResponseHandler {
  
  public static void sendErrorMessage(HttpServletResponse response, String error) throws IOException {
    Map<String, String> jsonErrorInfo = new HashMap<>();
    jsonErrorInfo.put("error", error);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonErrorInfo));
  }
}
