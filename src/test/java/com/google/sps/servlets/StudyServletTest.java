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
public final class StudyServletTest {

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
  private StudyServlet servlet;
  private DatastoreService datastore;
  private Card cardA, cardB, cardC;

  @Before
  public void setUp() throws Exception {
    // Generate cards that will be used
    cardA =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test")
            .setTextTranslated("translatedTest")
            .build();
    cardB =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test2")
            .setTextTranslated("translatedTest2")
            .build();
    cardC =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("test3")
            .setTextTranslated("translatedTest3")
            .build();

    helper.setUp();
    servlet = new StudyServlet();
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
  public void grabStudyModeQuiz() throws Exception {

    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    when(mockRequest.getParameter("numCards")).thenReturn("1");
    when(mockRequest.getParameter("numRounds")).thenReturn("2");

    List<Card> cards = new ArrayList<>();
    Card cardAInDatastore = storeCardInDatastore(cardA, datastore, folderKey);
    Card cardBInDatastore = storeCardInDatastore(cardB, datastore, folderKey);
    Card cardCInDatastore = storeCardInDatastore(cardC, datastore, folderKey);
    cards.add(cardAInDatastore);
    cards.add(cardBInDatastore);
    cards.add(cardCInDatastore);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    System.out.println("grabStudyModeQuiz-----------------");
    System.out.println(response);
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
  public void folderHasNoCards() throws Exception {
    List<Card> noCardsInDatastore = new ArrayList<>();

    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    servlet.doGet(mockRequest, mockResponse);
    System.out.println("folderHasNoCards-----------------");
    String response = responseWriter.toString();
    System.out.println(response);
    String expectedResponse = "";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    helper.setEnvIsLoggedIn(false);
    List<Card> noCardsQueried = new ArrayList<>();

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    System.out.println(response);
    System.out.println("UserNotLoggedIn-----------------");
    String expectedResponse = "";

    assertTrue(compareJson(response, expectedResponse));
  }

  public Card storeCardInDatastore(Card card, DatastoreService datastore, String folderKey) {
    card.setParentKey(folderKey);
    Entity cardEntity = card.createEntity();
    datastore.put(cardEntity);

    card.setCardKey(KeyFactory.keyToString(cardEntity.getKey()));

    return card;
  }
}
