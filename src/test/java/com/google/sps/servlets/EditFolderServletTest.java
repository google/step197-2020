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
public final class EditFolderServletTest {

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
  private EditFolderServlet servlet;
  private DatastoreService datastore;
    
  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new EditFolderServlet();
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
  public void EditFolder() throws Exception {

    Folder currentFolder = new Folder("FIRSTFOLDER", "en");
    Folder expectedFolder = new Folder("EDITEDFOLDER", "es_edited");
    
    // Generate testing User
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    Folder folderInDatastore = EntityTestingTool.populateDatastoreWithAFolder(currentFolder, datastore, userKey);
    String folderKey = folderInDatastore.getFolderKey();

    // Make sure the expected Folder has the same key
    expectedFolder.setFolderKey(folderKey);

    when(mockRequest.getParameter("folderName")).thenReturn("EDITEDFOLDER");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("es_edited");
    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);

    servlet.doPut(mockRequest, mockResponse);

    Entity editedFolder = datastore.get(KeyFactory.stringToKey(folderKey));

    String response = new Gson().toJson(new Folder(editedFolder));
    String expectedResponse = new Gson().toJson(expectedFolder);

    assertEquals(response, expectedResponse);
  }

}