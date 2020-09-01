package com.google.sps.servlets;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.sps.data.Folder;
import com.google.sps.data.Card;

@RunWith(JUnit4.class)
public final class DeleteFolderServletTest {

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
  private DeleteFolderServlet servlet;
  private DatastoreService datastore;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new DeleteFolderServlet();
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
  public void deleteFolderWithNoCardInDatastore() throws Exception {
    // Generate testing folder to add into datastore
    Folder folder = new Folder("FIRSTFOLDER", "en");

    // Generate a user entity to obtain a user key
    // which would be used to set as the parent of the folder entity
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    Folder.storeFolderInDatastore(folder, datastore, userKey);
    assertNotNull(datastore.get(KeyFactory.stringToKey(folder.getFolderKey())));
    String folderKey = folder.getFolderKey();

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(
        0,
        datastore
            .prepare(new Query("Folder").setAncestor(user.getKey()))
            .countEntities(withLimit(10)));
  }

  @Test
  public void deleteFolderAndItsCards() throws Exception {
    // Generate testing folder to add into datastore
    Folder folder = new Folder("FIRSTFOLDER", "en");

    // Generate card to add into datastore
    Card card =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("hello")
            .setTextTranslated("hola")
            .build();

    // Generate a user entity to obtain a user key
    // which would be used to set as the parent of the folder entity
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    Folder.storeFolderInDatastore(folder, datastore, userKey);
    assertNotNull(datastore.get(KeyFactory.stringToKey(folder.getFolderKey())));
    String folderKey = folder.getFolderKey();

    Card.storeCardInDatastore(card, datastore, folderKey);
    assertNotNull(datastore.get(KeyFactory.stringToKey(card.getCardKey())));

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(
        0,
        datastore
            .prepare(new Query("Folder").setAncestor(user.getKey()))
            .countEntities(withLimit(10)));
    assertEquals(
        0,
        datastore
            .prepare(new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey)))
            .countEntities(withLimit(10)));
  }
}
