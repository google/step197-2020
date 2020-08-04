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
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;

@RunWith(JUnit4.class)
public final class UploadOnBlobstoreServletTest {

  private final LocalServiceTestHelper helper = 
    new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig());

  private String BLOBSTORE_HEADER_URL = "http://localhost:8080/_ah/upload/";
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
  public void GetBlobstoreUploadUrl() throws Exception {
    // Note: blobstore url changes during local testing
    //       so we're just checking if there is a response
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    assertTrue(response.contains(BLOBSTORE_HEADER_URL));
  }
}