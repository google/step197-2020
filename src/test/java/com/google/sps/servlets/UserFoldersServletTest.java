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
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.gson.Gson;
import com.google.common.collect.ImmutableMap;
import com.google.sps.data.Folder;
import com.google.sps.tool.EntityTestingTool;
import java.util.List; 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


@RunWith(JUnit4.class)
public final class UserFoldersServletTest {

  private final LocalServiceTestHelper helper = 
    new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig()
        .setDefaultHighRepJobPolicyUnappliedJobPercentage(0),
      new LocalUserServiceTestConfig())
      .setEnvIsAdmin(true).setEnvIsLoggedIn(true)
      .setEnvEmail("test@gmail.com").setEnvAuthDomain("gmail.com");

  private static final Folder FOLDER_A = new Folder("FIRSTFOLDER", "en");
  private static final Folder FOLDER_B = new Folder("SECONDFOLDER", "en");
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private StringWriter responseWriter;
  private UserFoldersServlet servlet;
  private DatastoreService datastore;
    
  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new UserFoldersServlet();
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
    helper.tearDown();
  }
  
  @Test
  public void QueryUserFolders() throws Exception {
    // Returns array of Folders and signals front-end to show Create Folder Option
    
    // Generate testing User
    Entity USER_A = new Entity("User", "testId");
    String USERKEY = KeyFactory.keyToString(USER_A.getKey());

    when(mockRequest.getParameter("userKey")).thenReturn(USERKEY);
    
    List<Folder> foldersInDatastore = EntityTestingTool.populateDatastoreWithFolders(FOLDER_A, FOLDER_B, datastore, USERKEY);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(getExpectedJsonInfo(foldersInDatastore, true));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserHasNoCurrentFolder() throws Exception {
    // Returns an empty array list and signals front-end to show Create Folder Option

    List<Folder> noFoldersInDatastore = new ArrayList<>();

    // Generate testing User
    Entity USER_A = new Entity("User", "testId");
    String USERKEY = KeyFactory.keyToString(USER_A.getKey());

    when(mockRequest.getParameter("userKey")).thenReturn(USERKEY);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(getExpectedJsonInfo(noFoldersInDatastore, true));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserNotLoggedIn() throws Exception {
    // Returns an empty array list and signals front-end to not show Create Folder Option

    helper.setEnvIsLoggedIn(false);
    List<Folder> noFoldersQueried = new ArrayList<>();

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(getExpectedJsonInfo(noFoldersQueried, false));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserCreatesFirstFolder() throws Exception {
    // First time user is creating a folder, so current number of folders should be 1

    // Generate testing User
    Entity USER_A = new Entity("User", "testId");
    String USERKEY = KeyFactory.keyToString(USER_A.getKey());

    when(mockRequest.getParameter("folderName")).thenReturn("Folder1");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("en");
    when(mockRequest.getParameter("userKey")).thenReturn(USERKEY);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(1, datastore.prepare(new Query("Folder").setAncestor(USER_A.getKey())).countEntities(withLimit(10)));
  }

  @Test
  public void UserCreatesAFolderAndHasMultipleFoldersAlready() throws Exception {
    // Return the current number of folders that user has after creating a folder

    // Generate testing User
    Entity USER_A = new Entity("User", "testId");
    String USERKEY = KeyFactory.keyToString(USER_A.getKey());

    List<Folder> foldersInDatastore = EntityTestingTool.populateDatastoreWithFolders(FOLDER_A, FOLDER_B, datastore, USERKEY);

    when(mockRequest.getParameter("folderName")).thenReturn("Folder1");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("en");
    when(mockRequest.getParameter("userKey")).thenReturn(USERKEY);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(3, datastore.prepare(new Query("Folder").setAncestor(USER_A.getKey())).countEntities(withLimit(10)));

  }

  public Map<String, Object> getExpectedJsonInfo(List<Folder> folders, boolean showCreateFormStatus) {

    Map<String, Object> expectedJsonInfo = new HashMap<>();
    expectedJsonInfo.put("showCreateFormStatus", showCreateFormStatus);
    expectedJsonInfo.put("userFolders", folders);

    return expectedJsonInfo;
  }
}