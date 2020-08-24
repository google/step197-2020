package com.google.sps.servlets;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.sps.tool.Tool;
import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;

@RunWith(JUnit4.class)
public class TranslationServletTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvIsAdmin(true)
          .setEnvIsLoggedIn(true)
          .setEnvEmail("test@gmail.com")
          .setEnvAuthDomain("gmail.com");

  private Map<String, String> expectedJsonInfo = new HashMap<>();
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private StringWriter responseWriter;
  private TranslationServlet servlet;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new TranslationServlet();
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
  public void translateHelloToSpanish() throws Exception {
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("toLang")).thenReturn("es");
    when(mockRequest.getParameter("fromLang")).thenReturn("en");
    expectedJsonInfo.put("translation", "Hola");

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(expectedJsonInfo);
    assertTrue(Tool.compareJson(response, expectedResponse));
  }
}
