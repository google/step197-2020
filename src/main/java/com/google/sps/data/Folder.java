package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public final class Folder {

    private String folderName;
    private String folderDefaultLanguage;
    private String folderKey;

    public Folder(
        String folderName,
        String folderDefaultLanguage) {
        
        this.folderName = folderName;
        this.folderDefaultLanguage = folderDefaultLanguage;
        this.folderKey = "null";
    }

    public String getFolderName() {
        return this.folderName;
    }

    public String getFolderDefaultLanguage() {
        return this.folderDefaultLanguage;
    }

    public String getFolderKey() {
        return this.folderKey;
    }

    public void setFolderName(String newFolderName) {
        this.folderName = newFolderName;
    }

    public void setFolderKey(String folderKey) {
        this.folderKey = folderKey;
    }

    public Entity createEntity(Key userKey) {
        
        // Set owner of folder 
        Entity folder = new Entity("Folder", userKey);

        // Store initial folder entity without properties to generate auto ID for entity
        // This is necessary to obtain a "complete" key
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(folder);

        folder.setProperty("folderName", this.folderName);
        folder.setProperty("folderDefaultLanguage", this.folderDefaultLanguage);
        folder.setProperty("folderKey", KeyFactory.keyToString(folder.getKey()));

        return folder;
    }

    // Returns a folder instance from a given Entity
    public static Folder EntityToFolder(Entity entity) {
        
        String folderName = (String) entity.getProperty("folderName");
        String folderDefaultLanguage = (String) entity.getProperty("folderDefaultLanguage");
        String folderKey = (String) entity.getProperty("folderKey");

        Folder folder = new Folder(folderName, folderDefaultLanguage);
        folder.setFolderKey(folderKey);

        return folder;
    }
}