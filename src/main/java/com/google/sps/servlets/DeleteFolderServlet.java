package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.sps.tool.ResponseSerializer;
import com.google.sps.data.Card;
import com.google.sps.data.Folder;
import com.google.sps.tool.BlobstoreUtil;

@WebServlet("/deletefolder")
public class DeleteFolderServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      ResponseSerializer.sendErrorJson(response, "User not logged in");
      return;
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String folderKey = request.getParameter("folderKey");
    Entity folder = getExistingFolderInDatastore(datastore, folderKey);
    if (folder == null) {
      ResponseSerializer.sendErrorJson(response, "Cannot delete Folder");
      return;
    } else {
      deleteAllCardsInsideFolder(folder);
      Folder.deleteFolderWithRetries(folder);
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

  private void deleteAllCardsInsideFolder(Entity folder) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query cardQuery = new Query("Card").setAncestor(folder.getKey());
    PreparedQuery results = datastore.prepare(cardQuery);

    // TODO(ngothomas): There is a bug with deleting blobs on the
    // test server. It throws exceptions.
    if (results != null) {
      for (Entity card : results.asIterable()) {
        String imageBlobKey = (String) card.getProperty("imageBlobKey");
        if (imageBlobKey != null) {
          BlobstoreUtil.deleteBlobWithRetries(imageBlobKey);
        }
        Card.deleteCardWithRetries(card);
      }
    }
  }
}
