package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public final class Card {

    private String id;
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
        card.setProperty("blobKey", this.blobKey);
        card.setProperty("labels", this.labels);
        card.setProperty("fromLang", this.fromLang);
        card.setProperty("toLang", this.toLang);
        card.setProperty("textNotTranslated", this.textNotTranslated);
        card.setProperty("textTranslated", this.textTranslated);
        card.setProperty("cardKey", KeyFactory.keyToString(card.getKey()));

        return card;
    }

    public static Card EntityToCard(Entity entity) {

        String blobKey = (String) entity.getProperty("blobKey");
        String labels = (String) entity.getProperty("labels");
        String fromLang = (String) entity.getProperty("fromLang");
        String toLang = (String) entity.getProperty("toLang");
        String textNotTranslated = (String) entity.getProperty("textNotTranslated");
        String textTranslated = (String) entity.getProperty("textTranslated");
        String cardKey = (String) entity.getProperty("cardKey");

        Card card = new Card(blobKey, labels, fromLang, toLang, textNotTranslated, textTranslated);
        card.setCardKey(cardKey);
        
        return card;
    }
}