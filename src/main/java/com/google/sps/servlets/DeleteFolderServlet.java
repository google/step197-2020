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
  
  private void deleteFolder(String folderKey) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.delete(KeyFactory.stringToKey(folderKey));
  }

  private void deleteAllCardsInsideFolder(String folderKey) throws IOException {

    Query cardQuery = new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(cardQuery);

    if (results != null) {
      for (Entity card: results.asIterable()) {
        String blobKey = (String) card.getProperty("blobKey");
        if (blobKey != "null") {
          deleteBlob(blobKey);
        }
        deleteCard(datastore, card);
      }   
    }
  }

  private void deleteBlob(String blobKey) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    BlobKey key = new BlobKey(blobKey);
    blobstoreService.delete(key);
  }

  private void deleteCard(DatastoreService datastore, Entity card) {
    datastore.delete(card.getKey());
  }
}