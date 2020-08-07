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
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.data.User;
import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap;


@RunWith(JUnit4.class)
public final class LoginServletTest {

  private static final String USER_ID = "testID";
  
  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig()
        .setDefaultHighRepJobPolicyUnappliedJobPercentage(0),
      new LocalUserServiceTestConfig())
      .setEnvIsAdmin(true).setEnvIsLoggedIn(true)
      .setEnvEmail("test@gmail.com").setEnvAuthDomain("gmail.com")
      .setEnvAttributes(
        new HashMap(
          ImmutableMap.of(
            "com.google.appengine.api.users.UserService.user_id_key", USER_ID)));

  private static final User LOGGED_IN_USER = new User(USER_ID, "test@gmail.com");
  private static final User LOGGED_OUT_USER = new User("null", "null");
  private static final String LOGOUTURL = "/_ah/logout?continue\u003d%2FhomePage";
  private static final String LOGINURL = "/_ah/login?continue\u003d%2FhomePage";
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private StringWriter responseWriter;
  private LoginServlet servlet;
  private DatastoreService datastore;


  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new LoginServlet();
    mockRequest = mock(HttpServletRequest.class);
    mockResponse = mock(HttpServletResponse.class);
    
    // Set up a fake HTTP response 
    responseWriter = new StringWriter();
    when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));

    // Initialize datastore
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void tearDown() throws Exception {
    helper.setEnvIsLoggedIn(true);
    helper.tearDown();
  }
  
  @Test
  public void userLoggedInAndInDatastore() throws Exception {
    storeUsertoDatastore(datastore, LOGGED_IN_USER);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    
    Key key = LOGGED_IN_USER.createEntity().getKey(); 
    LOGGED_IN_USER.setUserKey(KeyFactory.keyToString(key));
    String expectedResponse = new Gson().toJson(getExpectedJsonInfo(
       /*user*/LOGGED_IN_USER, /*tabStatus*/true, /*logoutUrl*/LOGOUTURL, /*loginUrl*/"null"));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userLoggedInButNotInDatastore() throws Exception {
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();

    Key key = LOGGED_IN_USER.createEntity().getKey(); 
    LOGGED_IN_USER.setUserKey(KeyFactory.keyToString(key));
    String expectedResponse = new Gson().toJson(getExpectedJsonInfo(
        /*user*/LOGGED_IN_USER, /*tabStatus*/true, /*logoutUrl*/LOGOUTURL, /*loginUrl*/"null"));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    helper.setEnvIsLoggedIn(false);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(getExpectedJsonInfo(
        /*user*/LOGGED_OUT_USER, /*tabStatus*/false, /*logoutUrl*/"null", /*loginUrl*/LOGINURL));

    assertTrue(compareJson(response, expectedResponse));
  }
  
  public Map<String, Object> getExpectedJsonInfo(User user, boolean tabStatus, String logoutUrl, String loginUrl) {
    Map<String, Object> expectedJsonInfo = new HashMap<>();
    expectedJsonInfo.put("userInfo", user);
    expectedJsonInfo.put("showTabStatus", tabStatus);
    expectedJsonInfo.put("logoutUrl", logoutUrl);
    expectedJsonInfo.put("loginUrl", loginUrl);

    return expectedJsonInfo;
  }

  public void storeUsertoDatastore(DatastoreService datastore, User user) {
    datastore.put(user.createEntity());
  }
}