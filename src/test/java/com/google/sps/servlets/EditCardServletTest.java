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
import com.google.sps.data.Card;

public final class EditCardServletTest {

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
  private EditCardServlet servlet;
  private DatastoreService datastore;
    
  @Before
  public void setUp() throws Exception {
    helper.setUp();
    servlet = new EditCardServlet();
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
  public void editCard() throws Exception {
    Card currentCard = new Card.Builder()
        .setBlobKey("null")
        .setLabels("spanish")
        .setFromLang("en")
        .setToLang("es")
        .setRawText("hello")
        .setTextTranslated("hola")
        .build();
    Card expectedCard = new Card.Builder()
        .setBlobKey("null")
        .setLabels("vietnamese")
        .setFromLang("en")
        .setToLang("vi")
        .setRawText("hello")
        .setTextTranslated("xin chào")
        .build();
    
    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entities
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    Card cardInDatastore = storeCardInDatastore(currentCard, datastore, folderKey);
    String cardKey = cardInDatastore.getCardKey();

    // Make sure the expected card has the same key
    expectedCard.setCardKey(cardKey);

    when(mockRequest.getParameter("labels")).thenReturn("vietnamese");
    when(mockRequest.getParameter("fromLang")).thenReturn("en");
    when(mockRequest.getParameter("toLang")).thenReturn("vi");
    when(mockRequest.getParameter("cardKey")).thenReturn(cardKey);
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("textTranslated")).thenReturn("xin chào");

    servlet.doPut(mockRequest, mockResponse);

    Entity editedCard = datastore.get(KeyFactory.stringToKey(cardKey));

    String response = new Gson().toJson(new Card(editedCard, cardKey));
    String expectedResponse = "{\"blobKey\":\"null\",\"labels\":\"vietnamese\",\"fromLang\":\"en\",\"toLang\":\"vi\",\"rawText\":\"hello\",\"textTranslated\":\"xin chào\",\"key\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAEM\"}";
    assertEquals(response, expectedResponse);
  }

  public Card storeCardInDatastore(Card card, DatastoreService datastore, String folderKey) {
    card.setParentKey(folderKey);
    Entity cardEntity = card.createEntity();
    datastore.put(cardEntity);
    
    card.setCardKey(KeyFactory.keyToString(cardEntity.getKey()));

    return card;
  }
}