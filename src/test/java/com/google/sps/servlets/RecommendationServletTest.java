package com.google.sps.servlets;

import static com.google.sps.tool.Tool.compareJson;
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

@RunWith(JUnit4.class)
public class RecommendationServletTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvIsAdmin(true)
          .setEnvIsLoggedIn(true)
          .setEnvEmail("test@gmail.com")
          .setEnvAuthDomain("gmail.com");

  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private StringWriter responseWriter;
  private RecommendationServlet servlet;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new RecommendationServlet();
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
  public void wordHasNoNearestNeighbors() throws Exception {
    when(mockRequest.getParameter("queryWord")).thenReturn("sdsagdsjhdgjmgf");
    when(mockRequest.getParameter("numOfWordsRequested")).thenReturn("10");

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"error\":\"Cannot find similar words at the moment\"}";
    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void queryForCandyTop10NearestNeighbors() throws Exception {
    when(mockRequest.getParameter("queryWord")).thenReturn("candy");
    when(mockRequest.getParameter("numOfWordsRequested")).thenReturn("10");

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse =
        "{\"candy\":"
            + "[\"candies\",\"sweets\",\"chocolate\",\"chocolates\",\"lollipops\","
            + "\"lollypops\",\"gobstoppers\",\"lollies\",\"snacks\",\"soda\"]}";
    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void queryForElephantTop15NearestNeighbors() throws Exception {
    when(mockRequest.getParameter("queryWord")).thenReturn("elephant");
    when(mockRequest.getParameter("numOfWordsRequested")).thenReturn("15");

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse =
        "{\"elephant\":"
            + "[\"elephants\",\"rhino\",\"pachyderm\",\"tiger\",\"rhinoceros\","
            + "\"tigers\",\"hippo\",\"gorilla\",\"giraffe\",\"rhinos\","
            + "\"hippopotamus\",\"pachyderms\",\"rhinoceroses\",\"orangutan\",\"lions\"]}";
    assertTrue(compareJson(response, expectedResponse));
  }
}
