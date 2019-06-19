package com.google.codeu.servlets;

import com.google.appengine.api.utils.SystemProperty;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServletUtils {
  static Properties GetProperties() throws IOException {
    if (SystemProperty.environment.value() ==
        SystemProperty.Environment.Value.Production) {
      return GetProductionProperties();
    }

    return GetDevelopmentProperties();
  }

  static Properties GetProductionProperties() throws IOException {
    InputStream input =
        new FileInputStream(new File("WEB-INF/secrets/keys.properties"));

    Properties props = new Properties();
    props.load(input);
    return props;
  }

  static Properties GetDevelopmentProperties() throws IOException {
    InputStream input = new FileInputStream(
        new File("WEB-INF/secrets/keys.development.properties"));

    Properties props = new Properties();
    props.load(input);
    return props;
  }
}
