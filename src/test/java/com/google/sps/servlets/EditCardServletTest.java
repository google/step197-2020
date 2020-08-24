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
import com.google.sps.data.Card;

@RunWith(JUnit4.class)
public final class EditCardServletTest {

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
    Card currentCard =
        new Card.Builder()
            .setImageBlobKey("null")
            .setRawText("hello")
            .setTextTranslated("hola")
            .build();

    // Generate a folder entity to obtain a folder key
    // which would be used to set as the parent of the card entities
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    Card cardInDatastore = Card.storeCardInDatastore(currentCard, datastore, folderKey);
    String cardKey = cardInDatastore.getCardKey();

    when(mockRequest.getParameter("cardKey")).thenReturn(cardKey);
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("textTranslated")).thenReturn("xin chào");

    servlet.doPut(mockRequest, mockResponse);

    Entity editedCard = datastore.get(KeyFactory.stringToKey(cardKey));

    String response = new Gson().toJson(new Card(editedCard));
    String expectedResponse =
        "{\"imageBlobKey\":\"null\",\"rawText\":\"hello\",\"textTranslated\":\"xin chào\",\"key\":\"agR0ZXN0chwLEgZGb2xkZXIiBnRlc3RJRAwLEgRDYXJkGAEM\"}";
    assertEquals(response, expectedResponse);
  }
}
