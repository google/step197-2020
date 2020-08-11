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
  public void queryUserFolders() throws Exception {
    /* Returns array of Folders and signals front-end to show Create Folder Option */

    // Generate testing folders to store in datastore
    Folder folderA = new Folder("FIRSTFOLDER", "en");
    Folder folderB = new Folder("SECONDFOLDER", "en");
    
    // Generate testing User
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    when(mockRequest.getParameter("userKey")).thenReturn(userKey);
    
    List<Folder> folders = new ArrayList<>();
    Folder folderAInDatastore = storeFolderInDatastore(folderA, datastore, userKey);
    Folder folderBInDatastore = storeFolderInDatastore(folderB, datastore, userKey);
    folders.add(folderAInDatastore);
    folders.add(folderBInDatastore);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = 
      "{\"userFolders\":["
        +  "{\"folderName\":\"FIRSTFOLDER\",\"folderDefaultLanguage\":\"en\",\"folderKey\":\""+ folderAInDatastore.getFolderKey() +"\"},"
        +  "{\"folderName\":\"SECONDFOLDER\",\"folderDefaultLanguage\":\"en\",\"folderKey\":\""+ folderBInDatastore.getFolderKey() +"\"}],"
        + "\"showCreateFormStatus\":true}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userHasNoCurrentFolder() throws Exception {
    /* Returns an empty array list and signals front-end to show Create Folder Option */

    List<Folder> noFoldersInDatastore = new ArrayList<>();
    
    // Generate testing User
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    when(mockRequest.getParameter("userKey")).thenReturn(userKey);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"userFolders\":[],\"showCreateFormStatus\":true}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    /* Returns an empty array list and signals front-end to not show Create Folder Option */

    helper.setEnvIsLoggedIn(false);
    List<Folder> noFoldersQueried = new ArrayList<>();

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"userFolders\":[],\"showCreateFormStatus\":false}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userCreatesFirstFolder() throws Exception {
    /* First time user is creating a folder, so current number of folders should be 1 */
    
    // Generate testing User
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    when(mockRequest.getParameter("folderName")).thenReturn("Folder1");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("en");
    when(mockRequest.getParameter("userKey")).thenReturn(userKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(1, datastore.prepare(new Query("Folder").setAncestor(user.getKey())).countEntities(withLimit(10)));
  }

  @Test
  public void userCreatesAFolderAndHasMultipleFoldersAlready() throws Exception {
    /* Return the current number of folders that user has after creating a folder */

    // Generate testing folders to store in datastore
    Folder folderA = new Folder("FIRSTFOLDER", "en");
    Folder folderB = new Folder("SECONDFOLDER", "en");
    
    // Generate testing User
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());
    
    Folder folderAInDatastore = storeFolderInDatastore(folderA, datastore, userKey);
    Folder folderBInDatastore = storeFolderInDatastore(folderB, datastore, userKey);

    when(mockRequest.getParameter("folderName")).thenReturn("Folder1");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("en");
    when(mockRequest.getParameter("userKey")).thenReturn(userKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(3, datastore.prepare(new Query("Folder").setAncestor(user.getKey())).countEntities(withLimit(10)));
  }

<<<<<<< HEAD
=======
  private Folder storeFolderInDatastore(Folder folder, DatastoreService datastore, String userKey) {
    folder.setParentKey(userKey);
    Entity folderEntity = folder.createEntity();
    datastore.put(folderEntity);

    folder.setFolderKey(KeyFactory.keyToString(folderEntity.getKey()));
    
    return folder;
  }
>>>>>>> tdd4
}