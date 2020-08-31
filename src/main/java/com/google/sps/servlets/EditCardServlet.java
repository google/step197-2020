package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
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
import java.util.Map;
import java.util.List;

@WebServlet("/editcard")
public class EditCardServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      Map<String, String> jsonErrorInfo = ResponseSerializer.getErrorJson("User not logged in");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      return;
    }

    String cardKey = request.getParameter("cardKey");
    String newRawText = request.getParameter("rawText");
    String newTextTranslated = request.getParameter("textTranslated");
    String newImageBlobKey = getImageBlobKey(request);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity card = getExistingCardInDatastore(response, datastore, cardKey);

    if (card == null) {
      Map<String, String> jsonErrorInfo =
          ResponseSerializer.getErrorJson("Cannot edit Card at the moment");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
    } else {
      updateCard(
          response, card, datastore, cardKey, newRawText, newTextTranslated, newImageBlobKey);
    }
  }

  private void updateCard(
      HttpServletResponse response,
      Entity card,
      DatastoreService datastore,
      String cardKey,
      String newRawText,
      String newTextTranslated,
      String newImageBlobKey)
      throws IOException {

    card.setProperty("cardKey", cardKey);
    card.setProperty("rawText", newRawText);
    card.setProperty("textTranslated", newTextTranslated);
    card.setProperty("imageBlobKey", newImageBlobKey);
    datastore.put(card);
  }

  private Entity getExistingCardInDatastore(
      HttpServletResponse response, DatastoreService datastore, String cardKey) throws IOException {
    try {
      return datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  private String getImageBlobKey(HttpServletRequest request) {
    // Method to determine whether or not this is a unit test or live server
    // Unit tests will always set blobKey to "null"
    // There should be no paramater testStatus in the live server thus returns null
    if (request.getParameter("testStatus") == null) {
      return getBlobKeyFromBlobstore(request, "image");
    } else {
      return "null";
    }
  }

  private String getBlobKeyFromBlobstore(HttpServletRequest request, String formInputElementName) {

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    // User submitted form without selecting a file, so we can't get a BlobKey. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so the blobInfo has 0 byte (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }
    return blobKey.getKeyString();
  }
}
