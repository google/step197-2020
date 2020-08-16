package com.google.sps.tool;

import java.io.IOException;
import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;

public class ResponseParser {

  public static String getErrorJson(String error) throws IOException {
    Map<String, String> jsonErrorInfo = new HashMap<>();
    jsonErrorInfo.put("error", error);
    return new Gson().toJson(jsonErrorInfo);
  }
}
