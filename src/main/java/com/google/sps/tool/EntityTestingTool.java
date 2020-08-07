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
      card.getProperty("toLang") != null &&
      card.getProperty("cardKey") != null);
  }

  public static List<Folder> populateDatastoreWithFolders(Folder folderA, Folder folderB, DatastoreService datastore, String userKey) {
    
    Entity folderEntityA = folderA.createEntity(KeyFactory.stringToKey(userKey));
    Entity folderEntityB = folderB.createEntity(KeyFactory.stringToKey(userKey));

    // Update entity in datastore 
    datastore.put(folderEntityA);
    datastore.put(folderEntityB);

    List<Folder> folders = new ArrayList<>();
    folders.add(new Folder(folderEntityA));
    folders.add(new Folder(folderEntityB));

    return folders;
  }

  public static Folder populateDatastoreWithAFolder(Folder FOLDER_A, DatastoreService datastore, String USERKEY) {
    
    Entity folderEntity = FOLDER_A.createEntity(KeyFactory.stringToKey(USERKEY));

    // Update entity in datastore 
    datastore.put(folderEntity);

    return new Folder(folderEntity);
  }

  public static List<Card> populateDatastoreWithCards(Card cardA, Card cardB, DatastoreService datastore, String folderKey) {

    
    Entity cardEntityA = cardA.createEntity(KeyFactory.stringToKey(folderKey));
    Entity cardEntityB = cardB.createEntity(KeyFactory.stringToKey(folderKey));

    // Update entity in datastore 
    datastore.put(cardEntityA);
    datastore.put(cardEntityB);

    List<Card> cards = new ArrayList<>();
    cards.add(new Card(cardEntityA));
    cards.add(new Card(cardEntityB));

    return cards;
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