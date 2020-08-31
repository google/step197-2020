package com.google.sps.tool;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class ResponseSerializer {

  public static Map<String, String> getErrorJson(String error) throws IOException {
    Map<String, String> jsonErrorInfo = new HashMap<>();
    jsonErrorInfo.put("error", error);
    return jsonErrorInfo;
  }
}
