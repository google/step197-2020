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
	private String parentKey;

	public Folder(
		String folderName,
		String folderDefaultLanguage) {
		
		this.folderName = folderName;
		this.folderDefaultLanguage = folderDefaultLanguage;
		this.folderKey = "null";
	}

	public Folder(Entity entity, String key) {
		this.folderName = (String) entity.getProperty("folderName");
		this.folderDefaultLanguage = (String) entity.getProperty("folderDefaultLanguage");
		this.folderKey = key;
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

		return folder;
	}
}