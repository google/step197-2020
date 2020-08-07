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
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

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
import com.google.sps.data.Card;
import com.google.sps.tool.EntityTestingTool;
import java.util.List; 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@RunWith(JUnit4.class)
public final class UserCardsServletTest {

  private final LocalServiceTestHelper helper = 
    new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig()
        .setDefaultHighRepJobPolicyUnappliedJobPercentage(0),
      new LocalUserServiceTestConfig(),
      new LocalBlobstoreServiceTestConfig())
      .setEnvIsAdmin(true).setEnvIsLoggedIn(true)
      .setEnvEmail("test@gmail.com").setEnvAuthDomain("gmail.com");
  
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

    // Initialize datastore
    datastore = DatastoreServiceFactory.getDatastoreService();

  }

  @After
  public void tearDown() throws Exception {
    helper.setEnvIsLoggedIn(true);
    helper.tearDown();
  }

  @Test
  public void QueryUserCards() throws Exception {
    // Returns array of Cards and signals front-end to show Create Card Option

    // Generate testing card objects to store in datastore
    Card cardA = new Card("null", "viet", "en", "vi", "test", "test");
    Card cardB = new Card("null", "viet", "en", "vi", "test", "test");
    
    // Generate a dummy Folder Entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    
    List<Card> cardsInDatastore = EntityTestingTool.populateDatastoreWithCards(cardA, cardB, datastore, folderKey);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(EntityTestingTool.getExpectedJsonCardInfo(
        /*card*/cardsInDatastore, /*showCreateFormStatus*/true));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserHasNoCurrentCard() throws Exception {
    // Returns an empty array list and signals front-end to show Create Card Option

    List<Card> noCardsInDatastore = new ArrayList<>();

    // Generate a dummy Folder Entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(EntityTestingTool.getExpectedJsonCardInfo(
        /*card*/noCardsInDatastore, /*showCreateFormStatus*/true));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserNotLoggedIn() throws Exception {
    // Returns an empty array list and signals front-end to not show Create Folder Option

    helper.setEnvIsLoggedIn(false);
    List<Card> noCardsQueried = new ArrayList<>();

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(EntityTestingTool.getExpectedJsonCardInfo(
        /*card*/noCardsQueried, /*showCreateFormStatus*/false));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserCreatesAHelloSpanishCard() throws Exception {

    // Generate a dummy Folder Entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());
    
    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    when(mockRequest.getParameter("labels")).thenReturn("spanish");
    when(mockRequest.getParameter("fromLang")).thenReturn("en");
    when(mockRequest.getParameter("toLang")).thenReturn("es");
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("translatedText")).thenReturn("hola");

    servlet.doPost(mockRequest, mockResponse);
    PreparedQuery responseEntity = datastore.prepare(new Query("Card").setAncestor(folder.getKey()));
    
    assertTrue(EntityTestingTool.checkForNoNullValues(responseEntity.asSingleEntity()));
    assertEquals(1, responseEntity.countEntities(withLimit(10)));
  }

  @Test
  public void UserCreatesACardAndHasOtherCardsBefore() throws Exception {
    // Making sure that User's current number of cards increase after creating a card

    // Generate testing card objects to store in datastore
    Card cardA = new Card("null", "viet", "en", "vi", "test", "test");
    Card cardB = new Card("null", "viet", "en", "vi", "test", "test");

    // Generate a dummy Folder Entity
    Entity folder = new Entity("Folder", "testID");
    String folderKey = KeyFactory.keyToString(folder.getKey());
    
    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("folderKey")).thenReturn(folderKey);
    when(mockRequest.getParameter("labels")).thenReturn("spanish");
    when(mockRequest.getParameter("fromLang")).thenReturn("en");
    when(mockRequest.getParameter("toLang")).thenReturn("es");
    when(mockRequest.getParameter("rawText")).thenReturn("hello");
    when(mockRequest.getParameter("translatedText")).thenReturn("hola");
    
    EntityTestingTool.populateDatastoreWithCards(cardA, cardB, datastore, folderKey);
    servlet.doPost(mockRequest, mockResponse);
    PreparedQuery responseEntity = datastore.prepare(new Query("Card").setAncestor(folder.getKey()));

    assertEquals(3, responseEntity.countEntities(withLimit(10)));
  }
}