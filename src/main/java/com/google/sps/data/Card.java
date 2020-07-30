package com.google.sps.data;

public final class Card {
    private String id;
    private String blobKey;
    private String labels;
    private String fromLang;
    private String toLang;
    private String textNotTranslated;
    private String textTranslated;
      
    public Card (
        String id,
        String blobKey,
        String labels,
        String fromLang,
        String toLang,
        String textNotTranslated,
        String textTranslated) {
            
      this.id = id;
      this.blobKey = blobKey;
      this.labels = labels;
      this.fromLang = fromLang;
      this.toLang = toLang;
      this.textNotTranslated = textNotTranslated;
      this.textTranslated = textTranslated;
    }

    public String getId() {
        return this.id;
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
}
