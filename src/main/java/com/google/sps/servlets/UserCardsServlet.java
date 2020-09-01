package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

import com.google.sps.data.Card;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/usercards")
public class UserCardsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    List<Card> userCards = new ArrayList<>();

    Map<String, Object> jsonInfo = new HashMap<>();
    jsonInfo.put("showCreateFormStatus", false);

    if (userService.isUserLoggedIn()) {
      String folderKey = request.getParameter("folderKey");

      Query cardQuery = new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey));

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(cardQuery);

      if (results != null) {
        for (Entity entity : results.asIterable()) {
          if ((boolean) entity.getProperty("deleted") == true) {
            continue;
          }
          Card card = new Card(entity);
          userCards.add(card);
        }
      }

      jsonInfo.put("showCreateFormStatus", true);
    }

    jsonInfo.put("userCards", userCards);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonInfo));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String folderKey = request.getParameter("folderKey");
      String rawText = request.getParameter("rawText");
      String textTranslated = request.getParameter("translatedText");
      String imageBlobKey = getImageBlobKey(request);
      Card card =
          new Card.Builder()
              .setImageBlobKey(imageBlobKey)
              .setRawText(rawText)
              .setTextTranslated(textTranslated)
              .setParentKey(folderKey)
              .build();

      Entity cardEntity = card.createEntity();

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(cardEntity);
      response.sendRedirect("/InsideFolder?folderKey=" + folderKey);
    }
  }

  private String getImageBlobKey(HttpServletRequest request) {
    // TODO(ngothomas): There is a bug with getting getBlobKey to work on test server
    // Unit tests will always set blobKey to "null"
    // There should be no paramater testStatus in the live server thus returns null
    String blobKey;

    if (request.getParameter("testStatus") == null) {
      blobKey = getBlobKeyFromBlobstore(request, "image");
    } else {
      blobKey = "null";
    }

    return blobKey;
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
