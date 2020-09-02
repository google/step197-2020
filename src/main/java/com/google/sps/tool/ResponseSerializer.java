package com.google.sps.tool;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;

public class ResponseSerializer {

  public static Map<String, String> getErrorJson(String error) throws IOException {
    Map<String, String> jsonErrorInfo = new HashMap<>();
    jsonErrorInfo.put("error", error);
    return jsonErrorInfo;
  }

  public static void sendErrorJson(HttpServletResponse response, String error) throws IOException {
    Map<String, String> jsonErrorInfo = getErrorJson(error);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonErrorInfo));
  }
}
