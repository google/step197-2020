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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

import com.google.sps.data.Folder;
import com.google.sps.data.Card;
import java.util.List; 
import java.util.ArrayList;
import java.util.Map;

/** **/
@WebServlet("/usercards")
public class UserCardsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Ensures user is authenticated
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {

        List<Card> userCards = new ArrayList<>();

        String folderKey = request.getParameter("folderKey");

        // Query all folders identified by the userKey
        Query cardQuery = new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey));

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(cardQuery);

        if (results != null) {
          for (Entity entity: results.asIterable()) {
            Card card = Card.EntityToCard(entity);
            userCards.add(card);
          }   
        }

        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(userCards));
    }
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {

        // if blobKey is null, user did not add an image
        String folderKey = request.getParameter("folderKey");
        String blobKey = getBlobKeyfromBlobstore(request, "image");
        // String labels = request.getParameter("labels");
        String fromLang = request.getParameter("fromLang");
        String toLang = request.getParameter("toLang");
        String textNotTranslated = request.getParameter("textNotTranslated");
        String textTranslated = translateText(textNotTranslated, toLang);

        Card card = new Card(blobKey, labels, fromLang, toLang, textNotTranslated, textTranslated);
        Entity cardEntity = card.createEntity(KeyFactory.stringToKey(folderKey));

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(cardEntity);
    }

  }

  private String translateText(String textNotTranslated, String toLang) {

    // Do the translation.
    Translate translate = TranslateOptions.getDefaultInstance().getService();
    Translation translation = 
        translate.translate(textNotTranslated, Translate.TranslateOption.targetLanguage(toLang));
    String translatedText = translation.getTranslatedText();

    return translatedText;
  }

  private String getBlobKeyfromBlobstore(HttpServletRequest request, String formInputElementName) {
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