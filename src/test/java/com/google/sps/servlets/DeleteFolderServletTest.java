package com.google.sps.servlets;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
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

    // Initialize datastore
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void DeleteFolderAndThereAreNoneLeft() throws Exception {
    // Generate testing folder to add into datastore
    Folder folder = new Folder("FIRSTFOLDER", "en");

    // Generate a user entity to obtain a user key
    // which would be used to set as the parent of the folder entity
    Entity user = new Entity("User", "testId");
    String userKey = KeyFactory.keyToString(user.getKey());

    Folder folderInDatastore = storeFolderInDatastore(folder, datastore, userKey);
    String folderKey = folderInDatastore.getFolderKey();

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(
        0,
        datastore
            .prepare(new Query("Folder").setAncestor(user.getKey()))
            .countEntities(withLimit(10)));
  }

  @Test
  public void DeleteFolderAndItsCards() throws Exception {
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

    Folder folderInDatastore = storeFolderInDatastore(folder, datastore, userKey);
    String folderKey = folderInDatastore.getFolderKey();

    storeCardInDatastore(card, datastore, folderKey);

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

  private Folder storeFolderInDatastore(Folder folder, DatastoreService datastore, String userKey) {
    folder.setParentKey(userKey);
    Entity folderEntity = folder.createEntity();
    datastore.put(folderEntity);

    folder.setFolderKey(KeyFactory.keyToString(folderEntity.getKey()));

    return folder;
  }

  public Card storeCardInDatastore(Card card, DatastoreService datastore, String folderKey) {
    card.setParentKey(folderKey);
    Entity cardEntity = card.createEntity();
    datastore.put(cardEntity);

    card.setCardKey(KeyFactory.keyToString(cardEntity.getKey()));

    return card;
  }
}
