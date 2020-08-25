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
import com.google.sps.data.Card;

@RunWith(JUnit4.class)
public final class DeleteCardServletTest {

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
  private DeleteCardServlet servlet;
  private DatastoreService datastore;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new DeleteCardServlet();
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
  public void deleteACardWithOneInDatastore() throws Exception {
    // Generate testing card to add into datastore
    Card card =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("hello")
            .setTextTranslated("hola")
            .build();

    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    Card.storeCardInDatastore(card, datastore, folderKey);
    assertNotNull(datastore.get(KeyFactory.stringToKey(card.getCardKey())));
    String cardKey = card.getCardKey();

    when(mockRequest.getParameter("cardKey")).thenReturn(cardKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(
        0,
        datastore
            .prepare(new Query("Card").setAncestor(folder.getKey()))
            .countEntities(withLimit(10)));
  }

  @Test
  public void deleteACardWithTwoInDatastore() throws Exception {
    // Generate testing cards to add into datastore
    Card cardA =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("hello")
            .setTextTranslated("hola")
            .build();
    Card cardB =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("hello")
            .setTextTranslated("xin chào")
            .build();

    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    Card.storeCardInDatastore(cardB, datastore, folderKey);
    assertNotNull(datastore.get(KeyFactory.stringToKey(cardB.getCardKey())));

    Card.storeCardInDatastore(cardA, datastore, folderKey);
    assertNotNull(datastore.get(KeyFactory.stringToKey(cardA.getCardKey())));
    String cardAKey = cardA.getCardKey();

    when(mockRequest.getParameter("cardKey")).thenReturn(cardAKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(
        1,
        datastore
            .prepare(new Query("Card").setAncestor(folder.getKey()))
            .countEntities(withLimit(10)));
  }
}
