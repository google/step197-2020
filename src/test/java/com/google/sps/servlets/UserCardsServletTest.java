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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.sps.data.Card;
import java.util.List;
import java.util.ArrayList;

@RunWith(JUnit4.class)
public final class UserCardsServletTest {

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
  private UserCardsServlet servlet;
  private DatastoreService datastore;

  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new UserCardsServlet();
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
  public void queryUserCards() throws Exception {
    // Generate testing card objects to store in datastore
    Card cardA =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test")
            .setTextTranslated("test")
            .build();
    Card cardB =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test")
            .setTextTranslated("test")
            .build();

    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);

    List<Card> cards = new ArrayList<>();
    Card cardAInDatastore = Card.storeCardInDatastore(cardA, datastore, folderKey);
    Card cardBInDatastore = Card.storeCardInDatastore(cardB, datastore, folderKey);
    cards.add(cardAInDatastore);
    cards.add(cardBInDatastore);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse =
        "{\"userCards\":"
            + "["
            + "{\"imageBlobKey\":\"null\",\"rawText\":\"test\",\"textTranslated\":\"test\",\"key\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAEM\"},"
            + "{\"imageBlobKey\":\"null\",\"rawText\":\"test\",\"textTranslated\":\"test\",\"key\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAIM\"}"
            + "],"
            + "\"showCreateFormStatus\":true}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userHasNoCurrentCard() throws Exception {
    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"userCards\":[],\"showCreateFormStatus\":true}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    helper.setEnvIsLoggedIn(false);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "{\"userCards\":[],\"showCreateFormStatus\":false}";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userCreatesAHelloSpanishCard() throws Exception {
    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entities
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("translatedText")).thenReturn("hola");

    servlet.doPost(mockRequest, mockResponse);
    PreparedQuery responseEntity =
        datastore.prepare(new Query("Card").setAncestor(folder.getKey()));
    Entity card = responseEntity.asSingleEntity();

    // Ensures the created card has all the properties in datastore
    assertTrue(
        (card.getProperty("imageBlobKey") != null
            && card.getProperty("textTranslated") != null
            && card.getProperty("rawText") != null));

    assertEquals(1, responseEntity.countEntities(withLimit(10)));
  }

  @Test
  public void userCreatesACardAndHasOtherCardsBefore() throws Exception {
    // Generate testing card objects to store in datastore
    Card cardA =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test")
            .setTextTranslated("test")
            .build();
    Card cardB =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test")
            .setTextTranslated("test")
            .build();

    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entities
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("translatedText")).thenReturn("hola");

    Card.storeCardInDatastore(cardA, datastore, folderKey);
    Card.storeCardInDatastore(cardB, datastore, folderKey);

    servlet.doPost(mockRequest, mockResponse);
    PreparedQuery responseEntity =
        datastore.prepare(new Query("Card").setAncestor(folder.getKey()));

    assertEquals(3, responseEntity.countEntities(withLimit(10)));
  }
}
