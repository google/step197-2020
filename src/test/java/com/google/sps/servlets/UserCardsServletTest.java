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
  
  private static final Card CARD_A = new Card("null", "viet", "en", "vi", "test", "test");
  private static final Card CARD_B = new Card("null", "viet", "en", "vi", "test", "test");
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
    
    // Generate a dummy Folder Entity
    Entity FOLDER_A = new Entity("Folder", "testID");
    String FOLDERKEY = KeyFactory.keyToString(FOLDER_A.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(FOLDERKEY);
    
    List<Card> cardsInDatastore = EntityTestingTool.populateDatastoreWithCards(CARD_A, CARD_B, datastore, FOLDERKEY);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(EntityTestingTool.getExpectedJsonCardInfo(cardsInDatastore, true));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserHasNoCurrentCard() throws Exception {
    // Returns an empty array list and signals front-end to show Create Card Option

    List<Card> noCardsInDatastore = new ArrayList<>();

    // Generate a dummy Folder Entity
    Entity FOLDER_A = new Entity("Folder", "testID");
    String FOLDERKEY = KeyFactory.keyToString(FOLDER_A.getKey());

    when(mockRequest.getParameter("folderKey")).thenReturn(FOLDERKEY);
    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(EntityTestingTool.getExpectedJsonCardInfo(noCardsInDatastore, true));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserNotLoggedIn() throws Exception {
    // Returns an empty array list and signals front-end to not show Create Folder Option

    helper.setEnvIsLoggedIn(false);
    List<Card> noCardsQueried = new ArrayList<>();

    servlet.doGet(mockRequest, mockResponse);
    String response = responseWriter.toString();
    String expectedResponse = new Gson().toJson(EntityTestingTool.getExpectedJsonCardInfo(noCardsQueried, false));

    assertTrue(compareJson(response, expectedResponse));
  }

  @Test
  public void UserCreatesAHelloSpanishCard() throws Exception {

    // Generate a dummy Folder Entity
    Entity FOLDER_A = new Entity("Folder", "testID");
    String FOLDERKEY = KeyFactory.keyToString(FOLDER_A.getKey());
    
    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("folderKey")).thenReturn(FOLDERKEY);
    when(mockRequest.getParameter("labels")).thenReturn("spanish");
    when(mockRequest.getParameter("fromLang")).thenReturn("en");
    when(mockRequest.getParameter("toLang")).thenReturn("es");
    when(mockRequest.getParameter("textNotTranslated")).thenReturn("hello");

    servlet.doPost(mockRequest, mockResponse);
    PreparedQuery responseEntity = datastore.prepare(new Query("Card").setAncestor(FOLDER_A.getKey()));
    
    assertTrue(EntityTestingTool.checkForNoNullValues(responseEntity.asSingleEntity()));
    assertEquals(1, responseEntity.countEntities(withLimit(10)));
  }

  @Test
  public void UserCreatesACardAndHasOtherCardsBefore() throws Exception {
    // Making sure that User's current number of cards increase after creating a card

    // Generate testing Folder
    Entity FOLDER_A = new Entity("Folder", "testID");
    String FOLDERKEY = KeyFactory.keyToString(FOLDER_A.getKey());
    
    when(mockRequest.getParameter("testStatus")).thenReturn("True");
    when(mockRequest.getParameter("folderKey")).thenReturn(FOLDERKEY);
    when(mockRequest.getParameter("labels")).thenReturn("spanish");
    when(mockRequest.getParameter("fromLang")).thenReturn("en");
    when(mockRequest.getParameter("toLang")).thenReturn("es");
    when(mockRequest.getParameter("textNotTranslated")).thenReturn("hello");
    
    EntityTestingTool.populateDatastoreWithCards(CARD_A, CARD_B, datastore, FOLDERKEY);
    servlet.doPost(mockRequest, mockResponse);
    PreparedQuery responseEntity = datastore.prepare(new Query("Card").setAncestor(FOLDER_A.getKey()));

    assertEquals(3, responseEntity.countEntities(withLimit(10)));
  }
}