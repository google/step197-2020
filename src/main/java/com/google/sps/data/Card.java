package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public final class Card {

  public static class Builder {
    private String blobKey = "null";
    private String rawText = "null";
    private String textTranslated = "null";
    private String key;
    private String parentKey;

    public Builder() {}

    public Builder setBlobKey(String blobKey) {
      this.blobKey = blobKey;
      return this;
    }

    public Builder setRawText(String rawText) {
      this.rawText = rawText;
      return this;
    }

    public Builder setTextTranslated(String textTranslated) {
      this.textTranslated = textTranslated;
      return this;
    }

    public Builder setCardKey(String key) {
      this.key = key;
      return this;
    }

    public Builder setParentKey(String parentkey) {
      this.parentKey = parentkey;
      return this;
    }

    public Card build() {
      Card card = new Card();
      card.blobKey = this.blobKey;
      card.rawText = this.rawText;
      card.textTranslated = this.textTranslated;
      card.parentKey = this.parentKey;
      
      return card;
    }
  }

  private String blobKey = "null";
  private String rawText = "null";
  private String textTranslated = "null";
  private String key;
  private String parentKey;
  
  private Card() {}

  public Card (
    String blobKey,
    String rawText,
    String textTranslated) {
        
    this.blobKey = blobKey;
    this.rawText = rawText;
    this.textTranslated = textTranslated;
  }

  public Card(Entity entity, String key){
    this.blobKey = (String) entity.getProperty("blobKey");
    this.rawText = (String) entity.getProperty("rawText");
    this.textTranslated = (String) entity.getProperty("textTranslated");
    this.key = key;
  }

  public String getBlobKey() {
    return this.blobKey;
  }

  public String getRawText() {
    return this.rawText;
  }

  public String getTextTranslated() {
    return this.textTranslated;
  }

  public String getCardKey() {
    return this.key;
  }

  public void setBlobKey(String newBlobKey) {
    this.blobKey = blobKey;
  }

  public void setNewText(String newText) {
    this.rawText = newText;
  }

  public void setTextTranslated(String newText) {
    this.textTranslated = newText;
  }

  public void setCardKey(String key) {
    this.key = key;
  }

  public void setParentKey(String key) {
    this.parentKey = key;
  }

  public Entity createEntity() {
    Entity card = new Entity("Card", KeyFactory.stringToKey(this.parentKey));
    card.setProperty("blobKey", this.blobKey);
    card.setProperty("rawText", this.rawText);
    card.setProperty("textTranslated", this.textTranslated);

    return card;
  }
}