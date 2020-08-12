package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public final class Card {

    public static class Builder {
        private String blobKey = "null";
        private String labels = "null";
        private String fromLang = "null";
        private String toLang = "null";
        private String rawText = "null";
        private String textTranslated = "null";
        private String key;
        private String parentKey;

        public Builder() {}

        public Builder setBlobKey(String blobKey) {
            this.blobKey = blobKey;
            return this;
        }

        public Builder setLabels(String labels) {
            this.labels = labels;
            return this;
        }

        public Builder setFromLang(String fromLang) {
            this.fromLang = fromLang;
            return this;
        }

        public Builder setToLang(String toLang) {
            this.toLang = toLang;
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
            card.labels = this.labels;
            card.fromLang = this.fromLang;
            card.toLang = this.toLang;
            card.rawText = this.rawText;
            card.textTranslated = this.textTranslated;
            card.parentKey = this.parentKey;
            
            return card;
        }
    }

    private String blobKey = "null";
    private String labels = "null";
    private String fromLang = "null";
    private String toLang = "null";
    private String rawText = "null";
    private String textTranslated = "null";
    private String key;
    private String parentKey;
    
    private Card() {}

    public Card (
        String blobKey,
        String labels,
        String fromLang,
        String toLang,
        String rawText,
        String textTranslated) {
            
        this.blobKey = blobKey;
        this.labels = labels;
        this.fromLang = fromLang;
        this.toLang = toLang;
        this.rawText = rawText;
        this.textTranslated = textTranslated;
    }

    public Card(Entity entity, String key){
        this.blobKey = (String) entity.getProperty("blobKey");
        this.labels = (String) entity.getProperty("labels");
        this.fromLang = (String) entity.getProperty("fromLang");
        this.toLang = (String) entity.getProperty("toLang");
        this.rawText = (String) entity.getProperty("rawText");
        this.textTranslated = (String) entity.getProperty("textTranslated");
        this.key = key;
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
        card.setProperty("labels", this.labels);
        card.setProperty("fromLang", this.fromLang);
        card.setProperty("toLang", this.toLang);
        card.setProperty("rawText", this.rawText);
        card.setProperty("textTranslated", this.textTranslated);

        return card;
    }
}