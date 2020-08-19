package com.google.sps.tool;

public class Tool {

  // Compare two JSON Objects
  public static boolean compareJson(String actual, String expected) {
    actual = actual.replaceAll("\\s", "");
    expected = expected.replaceAll("\\s", "");

    return actual.equals(expected);
  }
}
