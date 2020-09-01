package com.google.sps.servlets;

import static org.junit.Assert.assertEquals;
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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.gson.Gson;
import com.google.sps.data.Folder;

@RunWith(JUnit4.class)
public final class EditFolderServletTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
              new LocalDatastoreServiceTestConfig()
                  .setDefaultHighRepJobPolicyUnappliedJobPercentage(0),
              new LocalUserServiceTestConfig())
          .setEnvIsAdmin(true)
          .setEnvIsLoggedIn(true)
          .setEnvEmail("test@gmail.com")
          .setEnvAuthDomain("gmail.com");

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

    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void editFolder() throws Exception {
    Folder currentFolder = new Folder("Folder", "en");

    // Generate a user entity to obtain a user key
    // which would be used to set as the parent of the folder entity
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    Folder folderInDatastore = Folder.storeFolderInDatastore(currentFolder, datastore, userKey);
    String folderKey = folderInDatastore.getFolderKey();

    when(mockRequest.getParameter("folderName")).thenReturn("Edited Folder");
    when(mockRequest.getParameter("folderDefaultLanguage")).thenReturn("es");
    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);

    servlet.doPut(mockRequest, mockResponse);

    Entity editedFolderEntity = datastore.get(KeyFactory.stringToKey(folderKey));
    Folder editedFolder = new Folder(editedFolderEntity);

    String response = new Gson().toJson(editedFolder);
    String expectedResponse =
        "{\"folderName\":\"Edited Folder\",\"folderDefaultLanguage\":\"es\",\"folderKey\":\"agR0ZXN0chwLEgRVc2VyIgZ0ZXN0SWQMCxIGRm9sZGVyGAEM\"}";

    assertEquals(response, expectedResponse);
  }
}
