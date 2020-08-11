package com.google.sps.tool;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.google.sps.data.Card;
import com.google.sps.data.Folder;

public class EntityTestingTool {

  public static boolean checkForNoNullValues(Entity card) {
    return (card.getProperty("blobKey") != null  &&
      card.getProperty("labels") != null &&
      card.getProperty("textTranslated") != null &&
      card.getProperty("rawText") != null &&
      card.getProperty("fromLang") != null &&
      card.getProperty("toLang") != null);
  }

  public static Folder populateDatastoreWithAFolder(Folder folder, DatastoreService datastore, String userKey) {
    
    Entity folderEntity = folder.createEntity(KeyFactory.stringToKey(userKey));

    // Update entity in datastore 
    datastore.put(folderEntity);

    Folder folderObject = new Folder(folderEntity);
    folderObject.setFolderKey(KeyFactory.keyToString(folderEntity.getKey()));
    
    return folderObject;
  }
  
  public static Card populateDatastoreWithACard(Card card, DatastoreService datastore, String folderKey) {
    
    Entity cardEntity = card.createEntity(KeyFactory.stringToKey(folderKey));

    // Update entity in datastore 
    datastore.put(cardEntity);

    Card cardObject = new Card(cardEntity);
    cardObject.setCardKey(KeyFactory.keyToString(cardEntity.getKey()));

    return cardObject;
  }

  public static Map<String, Object> getExpectedJsonFolderInfo(List<Folder> folders, boolean showCreateFormStatus) {

    Map<String, Object> expectedJsonInfo = new HashMap<>();
    expectedJsonInfo.put("showCreateFormStatus", showCreateFormStatus);
    expectedJsonInfo.put("userFolders", folders);

    return expectedJsonInfo;
  }

  public static Map<String, Object> getExpectedJsonCardInfo(List<Card> cards, boolean showCreateFormStatus) {

    Map<String, Object> expectedJsonInfo = new HashMap<>();
    expectedJsonInfo.put("showCreateFormStatus", showCreateFormStatus);
    expectedJsonInfo.put("userCards", cards);

    return expectedJsonInfo;
  }
}