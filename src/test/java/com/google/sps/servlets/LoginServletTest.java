package com.google.sps.servlets;

import static com.google.sps.tool.Tool.compareJson;
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
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.data.User;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap;

@RunWith(JUnit4.class)
public final class LoginServletTest {

  private static final String USER_ID = "testID";
  private static final User LOGGED_IN_USER = new User(USER_ID, "test@gmail.com");
  private static final User LOGGED_OUT_USER = new User("null", "null");

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
    String expectedResponse = "{\"logoutUrl\":\"/_ah/logout?continue\\u003d%2F\",\"loginUrl\":\"null\",\"showTabStatus\":true}";
    System.out.println(response);

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userLoggedInButNotInDatastore() throws Exception {
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"logoutUrl\":\"/_ah/logout?continue\\u003d%2F\",\"loginUrl\":\"null\",\"showTabStatus\":true}";
    System.out.println(response);

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    helper.setEnvIsLoggedIn(false);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"logoutUrl\":\"null\",\"loginUrl\":\"/_ah/login?continue\\u003d%2F\",\"showTabStatus\":false}";

    assertTrue(compareJson(response, expectedResponse));
  }

  public void storeUsertoDatastore(DatastoreService datastore, User user) {
    datastore.put(user.createEntity());
  }
}