package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

public final class Card {

  public static class Builder {
    private String imageBlobKey = "null";
    private String rawText = "null";
    private String textTranslated = "null";
    private String key;
    private String parentKey;
    private Double familiarityScore;
    private Long timeTested;

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

    public Builder setFamiliarityScore(Double newScore) {
      this.familiarityScore = newScore;
      return this;
    }

    public Builder setTimeTested(long time) {
      this.timeTested = time;
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
      card.key = this.key;
      card.parentKey = this.parentKey;
      card.familiarityScore = this.familiarityScore;
      card.timeTested = this.timeTested;
      return card;
    }
  }

  private String imageBlobKey = "null";
  private String rawText = "null";
  private String textTranslated = "null";
  private String key;
  private String parentKey;
  private Double familiarityScore;
  private Long timeTested;

  private Card() {}

  public Card(Entity entity) {
    this.imageBlobKey = (String) entity.getProperty("imageBlobKey");
    this.rawText = (String) entity.getProperty("rawText");
    this.textTranslated = (String) entity.getProperty("textTranslated");
    this.key = KeyFactory.keyToString(entity.getKey());
    this.familiarityScore = (Double) entity.getProperty("familiarityScore");
    this.timeTested = (Long) entity.getProperty("timeTested");
  }

  public Double getFamiliarityScore() {
    return this.familiarityScore;
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

  public Long getTimeTested() {
    return this.timeTested;
  }

  public String getCardKey() {
    return this.key;
  }

  public void setFamiliarityScore(Double newScore) {
    this.familiarityScore = newScore;
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

  public void setTimeTested(long time) {
    this.timeTested = time;
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
    if (this.familiarityScore != null) {
      card.setProperty("familiarityScore", this.familiarityScore);
      card.setProperty("timeTested", this.timeTested);
    }
    return card;
  }
}
