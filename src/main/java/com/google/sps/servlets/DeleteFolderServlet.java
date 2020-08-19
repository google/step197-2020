package com.google.sps.servlets;

import static com.google.appengine.api.datastore.TransactionOptions.Builder.*;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

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

@WebServlet("/deletefolder")
public class DeleteFolderServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String folderKey = request.getParameter("folderKey");
      deleteAllCardsInsideFolder(folderKey);
      deleteFolder(folderKey);
    }
  }

  private void deleteFolder(String folderKey) {
    int retries = 5;
    while (true) {
      try {
        folderDeletionTransaction(folderKey);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          break;
        }
        --retries;
      }
    }
  }

  private void folderDeletionTransaction(String folderKey) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction(withXG(true));
    try {
      datastore.delete(KeyFactory.stringToKey(folderKey));
      txn.commit();
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
  }

  private void deleteAllCardsInsideFolder(String folderKey) throws IOException {
    Query cardQuery = new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(cardQuery);

    // TODO(ngothomas): bug with testing local blobstore
    if (results != null) {
      for (Entity card : results.asIterable()) {
        String imageBlobKey = (String) card.getProperty("imageBlobKey");
        if (imageBlobKey != "null") {
          deleteBlob(imageBlobKey);
        }
        deleteCard(datastore, card);
      }
    }
  }

  private void deleteBlob(String blobKey) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    BlobKey key = new BlobKey(blobKey);

    // BlobstoreService doesn't have its own transaction API
    // So we will try to delete the blobKey within 5 tries
    // If it doesn't happen successfully, we will just continue w/o deleting
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
