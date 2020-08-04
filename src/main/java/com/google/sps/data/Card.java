package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public final class Card {

    private String blobKey;
    private String labels;
    private String fromLang;
    private String toLang;
    private String textNotTranslated;
    private String textTranslated;
    private String cardKey;
      
    public Card (
        String blobKey,
        String labels,
        String fromLang,
        String toLang,
        String textNotTranslated,
        String textTranslated) {
            
      this.blobKey = blobKey;
      this.labels = labels;
      this.fromLang = fromLang;
      this.toLang = toLang;
      this.textNotTranslated = textNotTranslated;
      this.textTranslated = textTranslated;
      this.cardKey = "null";
    }

    public Card(Entity entity){

      this.blobKey = (String) entity.getProperty("blobKey");
      this.labels = (String) entity.getProperty("labels");
      this.fromLang = (String) entity.getProperty("fromLang");
      this.toLang = (String) entity.getProperty("toLang");
      this.textNotTranslated = (String) entity.getProperty("textNotTranslated");
      this.textTranslated = (String) entity.getProperty("textTranslated");
      this.cardKey = (String) entity.getProperty("cardKey");
    }

    public String getBlobKey() {
        return this.blobKey;
    }

    public String getLabels() {
        return this.labels;
    }

    public String getFromLang() {
        return this.fromLang;
    }

    public String getToLang() {
        return this.toLang;
    }

    public String getTextNotTranslated() {
        return this.textNotTranslated;
    }

    public String getTextTranslated() {
        return this.textTranslated;
    }

    public String getCardKey() {
        return this.cardKey;
    }

    public void setBlobKey(String newBlobKey) {
        this.blobKey = blobKey;
    }

    public void setLabels(String newLabels) {
        // This may need to changed for further processing of how we should store labels // query them
        this.labels = newLabels;
    }

    public void setFromLang(String newLang) {
        this.fromLang = newLang;
    }
    
    public void setToLang(String newLang) {
        this.toLang = newLang;
    }

    public void setNewText(String newText) {
        this.textNotTranslated = newText;
    }

    public void setTextTranslated(String newText) {
        this.textTranslated = newText;
    }

    public void setCardKey(String cardKey) {
        this.cardKey = cardKey;
    }

    public Entity createEntity(Key folderKey) {
        
        Entity card = new Entity("Card", folderKey);

        // Store initial card entity without properties to generate auto ID for entity
        // This is necessary to obtain a "complete" key
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(card);

        card.setProperty("blobKey", this.blobKey);
        card.setProperty("labels", this.labels);
        card.setProperty("fromLang", this.fromLang);
        card.setProperty("toLang", this.toLang);
        card.setProperty("textNotTranslated", this.textNotTranslated);
        card.setProperty("textTranslated", this.textTranslated);
        card.setProperty("cardKey", KeyFactory.keyToString(card.getKey()));

        return card;
    }
}