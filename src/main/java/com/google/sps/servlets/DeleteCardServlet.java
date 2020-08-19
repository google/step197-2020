package com.google.sps.servlets;

import static com.google.appengine.api.datastore.TransactionOptions.Builder.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.tool.ResponseSerializer;

@WebServlet("/deletecard")
public class DeleteCardServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      String cardKey = request.getParameter("cardKey");
      Entity card = getExistingCardInDatastore(datastore, cardKey);

      if (card == null) {
        String jsonErrorInfo = ResponseSerializer.getErrorJson("Cannot delete Card at the moment");
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      } else {
        deleteBlob(card);
        deleteCard(card);
      }
    }
  }

  private Entity getExistingCardInDatastore(DatastoreService datastore, String cardKey)
      throws IOException {
    try {
      return datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  private void deleteBlob(Entity card) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String imageBlobKey = (String) card.getProperty("imageBlobKey");
    if (imageBlobKey != "null") {
      BlobKey key = new BlobKey(imageBlobKey);
      int retries = 5;
      while (true) {
        try {
          blobstoreService.delete(key);
          break;
        } catch (BlobstoreFailureException e) {
          if (retries == 0) {
            addBlobstoreTaskToQueue(key.getKeyString());
            break;
          }
          --retries;
        }
      }
    }
  }

  private void deleteCard(Entity card) {
    int retries = 5;
    while (true) {
      try {
        cardDeletionTransaction(card);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          addDatastoreTaskToQueue(card);
          break;
        }
        --retries;
      }
    }
  }

  private void cardDeletionTransaction(Entity card) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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

  private void addDatastoreTaskToQueue(Entity entity) {
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(
        TaskOptions.Builder.withUrl("/datastoreWorker")
            .param("key", KeyFactory.keyToString(entity.getKey())));
  }

  private void addBlobstoreTaskToQueue(String key) {
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(TaskOptions.Builder.withUrl("/blobstoreWorker").param("key", key));
  }
}
