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
import com.google.sps.data.Folder;
import com.google.sps.data.Card;
import com.google.sps.tool.EntityTestingTool;
import java.util.List; 
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


@RunWith(JUnit4.class)
public final class DeleteCardServletTest {

  private final LocalServiceTestHelper helper = 
    new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig()
        .setDefaultHighRepJobPolicyUnappliedJobPercentage(0),
      new LocalUserServiceTestConfig())
      .setEnvIsAdmin(true).setEnvIsLoggedIn(true)
      .setEnvEmail("test@gmail.com").setEnvAuthDomain("gmail.com");

  private static final Card CARD_A = new Card("null", "spanish", "en", "es", "hello", "hola");
  private static final Card CARD_B = new Card("null", "vietnamese", "en", "vi", "hello", "xin chào");
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

    // Initialize datastore
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void DeleteCardAndThereAreNoneLeft() throws Exception {
    // Currently only one card in datastore before deletion
    // Return: 0 card
    
    // Generate a dummy Folder Entity
    Entity FOLDER_A = new Entity("Folder", "testID");
    String FOLDERKEY = KeyFactory.keyToString(FOLDER_A.getKey());

    Card cardInDatastore = EntityTestingTool.populateDatastoreWithACard(CARD_A, datastore, FOLDERKEY);
    String cardKey = cardInDatastore.getCardKey();

    when(mockRequest.getParameter("cardKey")).thenReturn(cardKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(0, datastore.prepare(new Query("Card").setAncestor(FOLDER_A.getKey())).countEntities(withLimit(10)));
  }

  @Test
  public void DeleteCardAndThereAreOneLeft() throws Exception {
    // Currently two cards in datastore before deletion
    // Return: 1 card left
    
    // Generate a dummy Folder Entity
    Entity FOLDER_A = new Entity("Folder", "testID");
    String FOLDERKEY = KeyFactory.keyToString(FOLDER_A.getKey());

    Card cardToDelete = EntityTestingTool.populateDatastoreWithACard(CARD_A, datastore, FOLDERKEY);
    String cardKey = cardToDelete.getCardKey();

    Card secondCard = EntityTestingTool.populateDatastoreWithACard(CARD_B, datastore, FOLDERKEY);

    when(mockRequest.getParameter("cardKey")).thenReturn(cardKey);

    servlet.doPost(mockRequest, mockResponse);
    assertEquals(1, datastore.prepare(new Query("Card").setAncestor(FOLDER_A.getKey())).countEntities(withLimit(10)));
  }
}