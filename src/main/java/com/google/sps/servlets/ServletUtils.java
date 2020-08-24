package com.google.sps.servlets;

import com.google.appengine.api.utils.SystemProperty;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {
  static boolean IsProduction() {
    return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
  }

  static Properties GetProperties() throws IOException {
    if (IsProduction()) {
      return GetProductionProperties();
    }

    return GetDevelopmentProperties();
  }

  static Properties GetProductionProperties() throws IOException {
    InputStream input = new FileInputStream(new File("WEB-INF/secrets/keys.properties"));

    Properties props = new Properties();
    props.load(input);
    return props;
  }

  static Properties GetDevelopmentProperties() throws IOException {
    InputStream input =
        new FileInputStream(new File("WEB-INF/secrets/keys.development.properties"));

    Properties props = new Properties();
    props.load(input);
    return props;
  }

  public static String BuildReactRoot() {
    return IsProduction() ? "/build/js/" : "http://localhost:9000/";
  }

  // Set attribute TITLE before calling RenderReact to add a custom page title.
  // Set attribute HEAD_HTML to add aribtrary HTML to the end of head.
  static void RenderReact(String module, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    request.setAttribute("SERVER_ROOT", BuildReactRoot());

    request.setAttribute("REACT_MODULE", module);
    request.getRequestDispatcher("/WEB-INF/react.jsp").forward(request, response);
  }
}
