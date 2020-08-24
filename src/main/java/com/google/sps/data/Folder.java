package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreFailureException;

import org.mockito.plugins.MockMaker.StaticMockControl;

public final class Folder {

  private String folderName;
  private String folderDefaultLanguage;
  private String folderKey;
  private String parentKey;

  public Folder(String folderName, String folderDefaultLanguage) {
    this.folderName = folderName;
    this.folderDefaultLanguage = folderDefaultLanguage;
    this.folderKey = "null";
  }

  public Folder(Entity entity) {
    this.folderName = (String) entity.getProperty("folderName");
    this.folderDefaultLanguage = (String) entity.getProperty("folderDefaultLanguage");
    this.folderKey = KeyFactory.keyToString(entity.getKey());
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

  public void setParentKey(String key) {
    this.parentKey = key;
  }

  public Entity createEntity() {
    Entity folder = new Entity("Folder", KeyFactory.stringToKey(this.parentKey));
    folder.setProperty("folderName", this.folderName);
    folder.setProperty("folderDefaultLanguage", this.folderDefaultLanguage);
    folder.setProperty("status", true);

    return folder;
  }

  public static Folder storeFolderInDatastore(
      Folder folder, DatastoreService datastore, String userKey) {
    folder.setParentKey(userKey);
    Entity folderEntity = folder.createEntity();
    datastore.put(folderEntity);

    folder.setFolderKey(KeyFactory.keyToString(folderEntity.getKey()));

    return folder;
  }

  public static void deleteFolder(Entity folder) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    markFolderAsDeleted(folder);
    try {
      datastore.delete(folder.getKey());
    } catch (DatastoreFailureException e) {
      throw e;
    }
  }

  public static void markFolderAsDeleted(Entity folder) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    folder.setProperty("status", false);
    datastore.put(folder);
  }

  public static void addDatastoreDeleteTaskToQueue(Entity entity) {
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(
        TaskOptions.Builder.withUrl("/datastoreWorker")
            .param("key", KeyFactory.keyToString(entity.getKey())));
  }
}
