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

    
}