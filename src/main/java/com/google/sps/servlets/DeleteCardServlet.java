package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.tool.ResponseHandler;
import com.google.sps.data.Folder;
import com.google.sps.data.Card;

@WebServlet("/deletecard")
public class DeleteCardServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String cardKey = request.getParameter("cardKey");
      deleteCardFromDatastore(response, cardKey);
    }
  }

  private void deleteCardFromDatastore(HttpServletResponse response, String cardKey) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity card = getExistingCardInDatastore(response, datastore, cardKey);
    deleteBlob(card);
    deleteCard(datastore, card);
  }

  private Entity getExistingCardInDatastore(HttpServletResponse response, DatastoreService datastore, String cardKey) throws IOException {
    Entity card;
    try {
      card = datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      ResponseHandler.sendErrorMessage(response, "Cannot edit Card at the moment");
      card = null;
    }

    return card;
  }

  private void deleteBlob(Entity card) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String blobKey = (String) card.getProperty("blobKey");
    if (blobKey != "null") {
      BlobKey key = new BlobKey(blobKey);
      blobstoreService.delete(key);
    }
  }

  private void deleteCard(DatastoreService datastore, Entity card) {
    datastore.delete(card.getKey());
  }
}