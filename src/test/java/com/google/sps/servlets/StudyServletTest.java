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
  public void grabNewStudyModeCards() throws Exception {
    /**
     * Cards have not been initialized and all have the same default familarity score. Therefore,
     * they will be returned in the order created.
     */
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

    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    // Response Should generate 2 rounds with 1 card each
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
    String expectedResponse =
        "["
            + "[{\"quizWord\":\"translatedTest\", \"cardKey\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAEM\","
            + "\"possibleResponses\":[\"test\"],\"correctAnswer\":\"test\"}],"
            + "[{\"quizWord\":\"translatedTest2\",\"cardKey\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAIM\","
            + "\"possibleResponses\":[\"test2\"],\"correctAnswer\":\"test2\"}]"
            + "]";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void grabStudyModeCards() throws Exception {
    /*
     * These cards  have familarity scores and should be sorted and
     * returned in order of increasing familarity score.
     */
    cardA =
        new Card.Builder()
            .setFamilarityScore(3.67)
            .setImageBlobKey("null")
            .setRawText("test")
            .setTextTranslated("translatedTest")
            .build();
    cardB =
        new Card.Builder()
            .setFamilarityScore(-.54)
            .setImageBlobKey("null")
            .setRawText("test2")
            .setTextTranslated("translatedTest2")
            .build();
    cardC =
        new Card.Builder()
            .setFamilarityScore(9.3)
            .setImageBlobKey("null")
            .setRawText("test3")
            .setTextTranslated("translatedTest3")
            .build();

    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    // Response Should generate 3 rounds with 1 card each
    when(mockRequest.getParameter("numCards")).thenReturn("1");
    when(mockRequest.getParameter("numRounds")).thenReturn("3");

    List<Card> cards = new ArrayList<>();
    Card cardAInDatastore = storeCardInDatastore(cardA, datastore, folderKey);
    Card cardBInDatastore = storeCardInDatastore(cardB, datastore, folderKey);
    Card cardCInDatastore = storeCardInDatastore(cardC, datastore, folderKey);
    cards.add(cardAInDatastore);
    cards.add(cardBInDatastore);
    cards.add(cardCInDatastore);

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    System.out.println("grabSortedStudyModeQuiz1-----------------");
    System.out.println(response);
    String expectedResponse =
        "["
            + "[{\"quizWord\":\"translatedTest2\", \"cardKey\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAIM\","
            + "\"possibleResponses\":[\"test2\"],\"correctAnswer\":\"test2\"}],"
            + "[{\"quizWord\":\"translatedTest\",\"cardKey\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAEM\","
            + "\"possibleResponses\":[\"test\"],\"correctAnswer\":\"test\"}],"
            + "[{\"quizWord\":\"translatedTest3\",\"cardKey\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAMM\","
            + "\"possibleResponses\":[\"test3\"],\"correctAnswer\":\"test3\"}]"
            + "]";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void folderHasNoCards() throws Exception {
    List<Card> noCardsInDatastore = new ArrayList<>();

    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = "[]";

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void userNotLoggedIn() throws Exception {
    helper.setEnvIsLoggedIn(false);
    List<Card> noCardsQueried = new ArrayList<>();

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
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
