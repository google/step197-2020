package com.google.sps.data;

public final class Folder {

    private String id;
    private String folderName;
    private String folderDefaultLanguage;

    public Folder(
        String id,
        String folderName,
        String folderDefaultLanguage) {
        
        this.id = id;
        this.folderName = folderName;
        this.folderDefaultLanguage = folderDefaultLanguage;
    }

    public String getId() {
        return this.id;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public String getFolderDefaultLanguage() {
        return this.folderDefaultLanguage;
    }

    public void setFolderName(String newFolderName) {
        this.folderName = newFolderName;
    }
    
}