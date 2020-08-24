package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.tool.ResponseSerializer;
import com.google.sps.data.Card;
import com.google.sps.data.Folder;

@WebServlet("/deletefolder")
public class DeleteFolderServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      String jsonErrorInfo = ResponseSerializer.getErrorJson("User not logged in");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      return;
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String folderKey = request.getParameter("folderKey");
    Entity folder = getExistingFolderInDatastore(datastore, folderKey);

    if (folder == null) {
      String jsonErrorInfo = ResponseSerializer.getErrorJson("Cannot delete Folder at the moment");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
    } else {
      deleteAllCardsInsideFolder(folder);
      deleteFolderWithRetries(folder);
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

  private void deleteFolderWithRetries(Entity folder) {
    int retries = 5;
    while (true) {
      try {
        deleteFolder(folder);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          Folder.addDatastoreDeleteTaskToQueue(folder);
          break;
        }
        --retries;
      }
    }
  }

  private void deleteFolder(Entity folder) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      datastore.delete(folder.getKey());
    } catch (DatastoreFailureException e) {
      throw e;
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
          Card.deleteBlob(imageBlobKey);
        }
        deleteCardWithRetries(card);
      }
    }
  }

  private void deleteCardWithRetries(Entity card) {
    int retries = 5;
    while (true) {
      try {
        deleteCard(card);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          Card.addDatastoreDeleteTaskToQueue(card);
          break;
        }
        --retries;
      }
    }
  }

  private void deleteCard(Entity card) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      datastore.delete(card.getKey());
    } catch (DatastoreFailureException e) {
      throw e;
    }
  }
}
