package com.google.sps.servlets;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;

@RunWith(JUnit4.class)
public final class UploadOnBlobstoreServletTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig());

  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private StringWriter responseWriter;
  private UploadOnBlobstoreServlet servlet;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new UploadOnBlobstoreServlet();
    mockRequest = mock(HttpServletRequest.class);
    mockResponse = mock(HttpServletResponse.class);

    // Set up a fake HTTP response
    responseWriter = new StringWriter();
    when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void getBlobstoreUploadUrl() throws Exception {
    // Blobstore url changes during local testing
    // So we're just checking if there is a response
    String blobstoreHeaderUrl = "http://localhost:8080/_ah/upload/";

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    assertTrue(response.contains(blobstoreHeaderUrl));
  }
}
