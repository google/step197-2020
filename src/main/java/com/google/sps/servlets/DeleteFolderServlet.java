package com.google.sps.servlets;

import static com.google.appengine.api.datastore.TransactionOptions.Builder.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
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

@WebServlet("/deletefolder")
public class DeleteFolderServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      String folderKey = request.getParameter("folderKey");
      Entity folder = getExistingFolderInDatastore(datastore, folderKey);

      if (folder == null) {
        String jsonErrorInfo =
            ResponseSerializer.getErrorJson("Cannot delete Folder at the moment");
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      } else {
        deleteAllCardsInsideFolder(folder);
        deleteFolder(folder);
      }
    }
  }

  private Entity getExistingFolderInDatastore(DatastoreService datastore, String cardKey)
      throws IOException {
    try {
      return datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  private void deleteFolder(Entity folder) {
    int retries = 5;
    while (true) {
      try {
        folderDeletionTransaction(folder);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          addDatastoreTaskToQueue(folder);
          break;
        }
        --retries;
      }
    }
  }

  private void folderDeletionTransaction(Entity folder) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction(withXG(true));
    try {
      datastore.delete(folder.getKey());
      txn.commit();
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
  }

  private void deleteAllCardsInsideFolder(Entity folder) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query cardQuery = new Query("Card").setAncestor(folder.getKey());
    PreparedQuery results = datastore.prepare(cardQuery);

    // TODO(ngothomas): bug with testing local blobstore
    // which is why there is a conditional statement for imageBlobKey
    if (results != null) {
      for (Entity card : results.asIterable()) {
        String imageBlobKey = (String) card.getProperty("imageBlobKey");
        if (imageBlobKey != "null") {
          deleteBlob(imageBlobKey);
        }
        deleteCard(card);
      }
    }
  }

  private void deleteBlob(String blobKey) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    BlobKey key = new BlobKey(blobKey);

    // BlobstoreService doesn't have its own transaction API
    // So we will try to delete the blobKey within 5 tries
    // If it doesn't happen successfully, we will add it to a TaskQueue
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
