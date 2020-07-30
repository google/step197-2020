package com.google.sps.servlets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static com.google.sps.tool.Tool.compareJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.User;
import com.google.gson.Gson;


@RunWith(JUnit4.class)
public final class LoginServletTest {
  
  private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvIsAdmin(true).setEnvIsLoggedIn(true)
          .setEnvEmail("test@gmail.com").setEnvAuthDomain("gmail.com");


  // Need to update this servlet test
  private static final User USER_A = new User("null", "/_ah/logout?continue\u003d%2F", "test@gmail.com", true);
  private static final User USER_B = new User("/_ah/login?continue\u003d%2F", "null", "null", false);
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private StringWriter responseWriter;
  private LoginServlet servlet;


  @Before
  public void setUp() throws Exception {
      helper.setUp();
      servlet = new LoginServlet();
      mockRequest = mock(HttpServletRequest.class);
      mockResponse = mock(HttpServletResponse.class);
      
      // Set up a fake HTTP response 
      responseWriter = new StringWriter();
      when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
  }

  @After
  public void tearDown() throws Exception {
      helper.setEnvIsLoggedIn(true);
      helper.tearDown();
  }

  @Test
  public void userLoggedIn() throws Exception {
      servlet.doGet(mockRequest, mockResponse);
      String response = responseWriter.toString();
      String expectedResponse = new Gson().toJson(USER_A);
      assertTrue(compareJson(response, expectedResponse));
  }
  
  @Test
  public void userNotLoggedIn() throws Exception {
      helper.setEnvIsLoggedIn(false);
      servlet.doGet(mockRequest, mockResponse);
      String response = responseWriter.toString();
      String expectedResponse = new Gson().toJson(USER_B);
      assertTrue(compareJson(response, expectedResponse));
  }
}