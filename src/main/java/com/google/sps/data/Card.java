package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public final class Card {

  public static class Builder {
    private String imageBlobKey = "null";
    private String rawText = "null";
    private String textTranslated = "null";
    private String key;
    private String parentKey;

    public Builder() {}

    public Builder setImageBlobKey(String imageBlobKey) {
      this.imageBlobKey = imageBlobKey;
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
      card.imageBlobKey = this.imageBlobKey;
      card.rawText = this.rawText;
      card.textTranslated = this.textTranslated;
      card.parentKey = this.parentKey;
      
      return card;
    }
  }

  private String imageBlobKey = "null";
  private String rawText = "null";
  private String textTranslated = "null";
  private String key;
  private String parentKey;
  
  private Card() {}

  public Card(Entity entity, String key){
    this.imageBlobKey = (String) entity.getProperty("imageBlobKey");
    this.rawText = (String) entity.getProperty("rawText");
    this.textTranslated = (String) entity.getProperty("textTranslated");
    this.key = key;
  }

  public String getImageBlobKey() {
    return this.imageBlobKey;
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

  public void setImageBlobKey(String newImageBlobKey) {
    this.imageBlobKey = newImageBlobKey;
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
    card.setProperty("imageBlobKey", this.imageBlobKey);
    card.setProperty("rawText", this.rawText);
    card.setProperty("textTranslated", this.textTranslated);

    return card;
  }
}