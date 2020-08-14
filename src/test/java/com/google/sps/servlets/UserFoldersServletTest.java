package com.google.sps.servlets;

import static com.google.sps.tool.Tool.compareJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.sps.data.Folder;
import java.util.List; 
import java.util.ArrayList;

@RunWith(JUnit4.class)
public final class UserFoldersServletTest {

  private static final String USER_KIND = "User";
  private static final String USER_ID = "testId";
  private static final String FOLDER_A = "First Folder";
  private static final String FOLDER_B = "Second Folder";
  private static final String FOLDER_LANGUAGE = "en";

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

    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }
  
  @Test
  public void queryUserFolders() throws Exception {
    // Generate testing folders to store in datastore
    Folder folderA = new Folder(FOLDER_A, FOLDER_LANGUAGE);
    Folder folderB = new Folder(FOLDER_B, FOLDER_LANGUAGE);
    
    // Generate testing user entity to query by email
	  // And generate a userKey to set as the parent of the testing folders
    Entity user = new Entity(USER_KIND, USER_ID);
    String userKey = KeyFactory.keyToString(user.getKey());
    user.setProperty("email", "test@gmail.com");
    datastore.put(user);
    
    List<Folder> folders = new ArrayList<>();
    Folder folderAInDatastore = storeFolderInDatastore(folderA, datastore, userKey);
    Folder folderBInDatastore = storeFolderInDatastore(folderB, datastore, userKey);
    folders.add(folderAInDatastore);
    folders.add(folderBInDatastore);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = 
      	"{\"userFolders\":["
          +  "{\"folderName\":\"First Folder\",\"folderDefaultLanguage\":\"en\",\"folderKey\":\""+ folderAInDatastore.getFolderKey() +"\"},"
          +  "{\"folderName\":\"Second Folder\",\"folderDefaultLanguage\":\"en\",\"folderKey\":\""+ folderBInDatastore.getFolderKey() +"\"}],"
          + "\"showCreateFormStatus\":true}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userHasNoCurrentFolder() throws Exception {
    // Generate testing user entity to query by email
	  // And generate a userKey to set as the parent of the testing folders
    Entity user = new Entity(USER_KIND, USER_ID);
    user.setProperty("email", "test@gmail.com");
    datastore.put(user);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"userFolders\":[],\"showCreateFormStatus\":true}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    helper.setEnvIsLoggedIn(false);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"userFolders\":[],\"showCreateFormStatus\":false}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userCreatesFirstFolder() throws Exception {
    // Generate testing user entity to query by email
    // And generate a userKey to set as the parent of the testing folders
    Entity user = new Entity(USER_KIND, USER_ID);
    user.setProperty("email", "test@gmail.com");
    datastore.put(user);

    when(mockRequest.getParameter("folderName")).thenReturn("Folder1");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("en");

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(1, datastore.prepare(new Query("Folder").setAncestor(user.getKey())).countEntities(withLimit(10)));
  }

  @Test
  public void userCreatesAFolderAndHasMultipleFoldersAlready() throws Exception {
    // Generate testing folders to store in datastore
    Folder folderA = new Folder(FOLDER_A, FOLDER_LANGUAGE);
    Folder folderB = new Folder(FOLDER_B, FOLDER_LANGUAGE);
    
    // Generate testing user entity to query by email
    // And generate a userKey to set as the parent of the testing folders
    Entity user = new Entity(USER_KIND, USER_ID);
    String userKey = KeyFactory.keyToString(user.getKey());
    user.setProperty("email", "test@gmail.com");
    datastore.put(user);
    
    storeFolderInDatastore(folderA, datastore, userKey);
    storeFolderInDatastore(folderB, datastore, userKey);

    when(mockRequest.getParameter("folderName")).thenReturn("Folder1");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("en");

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(3, datastore.prepare(new Query("Folder").setAncestor(user.getKey())).countEntities(withLimit(10)));
  }

  private Folder storeFolderInDatastore(Folder folder, DatastoreService datastore, String userKey) {
    folder.setParentKey(userKey);
    Entity folderEntity = folder.createEntity();
    datastore.put(folderEntity);

    folder.setFolderKey(KeyFactory.keyToString(folderEntity.getKey()));
    
    return folder;
  }
}