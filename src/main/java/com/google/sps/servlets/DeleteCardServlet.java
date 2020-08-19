package com.google.sps.servlets;

import static com.google.appengine.api.datastore.TransactionOptions.Builder.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
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
import com.google.sps.tool.ResponseSerializer;
import com.google.sps.data.Folder;
import com.google.sps.data.Card;

@WebServlet("/deletecard")
public class DeleteCardServlet extends HttpServlet {
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      String cardKey = request.getParameter("cardKey");
      Entity card = getExistingCardInDatastore(response, datastore, cardKey);
  
      if (card == null) {
        String jsonErrorInfo = ResponseSerializer.getErrorJson("Cannot delete Card at the moment");
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      } else {
        deleteBlob(card);
        deleteCard(datastore, card);
      }
    }
  }

  private Entity getExistingCardInDatastore(HttpServletResponse response, DatastoreService datastore, String cardKey) throws IOException {
    try {
      return datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  private void deleteBlob(Entity card) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String blobKey = (String) card.getProperty("blobKey");
    if (blobKey != "null") {
      BlobKey key = new BlobKey(blobKey);
      int retries = 5;
      while (true) {
        try {
          blobstoreService.delete(key);
          break;
        } catch (BlobstoreFailureException e) {
          if (retries == 0) {
            break;
          }
          --retries;
        }
      }
    }
  }

  private void deleteCard(DatastoreService datastore, Entity card) {
    int retries = 5;
    while (true) {
      try {
        cardDeletionTransaction(datastore, card);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          break;
        }
        --retries;
      }
    }
  }

  private void cardDeletionTransaction(DatastoreService datastore, Entity card) {
    Transaction txn = datastore.beginTransaction(withXG(true));
    try {
      datastore.delete(card.getKey());
      txn.commit();
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
  }
}