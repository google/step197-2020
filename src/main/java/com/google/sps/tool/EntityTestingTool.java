package com.google.sps.tool;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.List;
import java.util.ArrayList;
import com.google.sps.data.Card;
import com.google.sps.data.Folder;

public class EntityTestingTool {

  public static boolean checkForNoNullValues(Entity card) {
    return (card.getProperty("blobKey") != null  &&
      card.getProperty("labels") != null &&
      card.getProperty("textTranslated") != null &&
      card.getProperty("textNotTranslated") != null &&
      card.getProperty("fromLang") != null &&
      card.getProperty("toLang") != null &&
      card.getProperty("cardKey") != null);
  }

  public static Folder populateDatastoreWithAFolder(Folder folder, DatastoreService datastore, String userKey) {
    
    Entity folderEntity = folder.createEntity(KeyFactory.stringToKey(userKey));

    // Update entity in datastore 
    datastore.put(folderEntity);

    Folder folderObject = new Folder(folderEntity);
    folderObject.setFolderKey(KeyFactory.keyToString(folderEntity.getKey()));
    
    return folderObject;
  }
  
  /*
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
  */

    
}