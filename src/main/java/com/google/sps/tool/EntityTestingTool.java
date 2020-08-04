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

  public static List<Folder> populateDatastoreWithFolders(Folder FOLDER_A, Folder FOLDER_B, DatastoreService datastore, String USERKEY) {
    
    Entity folderA_Entity = FOLDER_A.createEntity(KeyFactory.stringToKey(USERKEY));
    Entity folderB_Entity = FOLDER_B.createEntity(KeyFactory.stringToKey(USERKEY));

    // Update entity in datastore 
    datastore.put(folderA_Entity);
    datastore.put(folderB_Entity);

    List<Folder> folders = new ArrayList<>();
    folders.add(Folder.EntityToFolder(folderA_Entity));
    folders.add(Folder.EntityToFolder(folderB_Entity));

    return folders;
  }

  public static List<Card> populateDatastoreWithCards(Card CARD_A, Card CARD_B, DatastoreService datastore, String FOLDERKEY) {
    
    Entity CardA_Entity = CARD_A.createEntity(KeyFactory.stringToKey(FOLDERKEY));
    Entity CardB_Entity = CARD_B.createEntity(KeyFactory.stringToKey(FOLDERKEY));

    // Update entity in datastore 
    datastore.put(CardA_Entity);
    datastore.put(CardB_Entity);

    List<Card> cards = new ArrayList<>();
    cards.add(Card.EntityToCard(CardA_Entity));
    cards.add(Card.EntityToCard(CardB_Entity));

    return cards;
  }

    
}