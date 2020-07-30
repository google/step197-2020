package com.google.sps.data;

/** An image object. */
public final class Image {

  private final long id;
  private final String name;
  private final String blobKey;
  private final long timestamp;
  private final String email;
  

  public Image(long id, String name, String blobKey, long timestamp, String email) {
    this.id = id;
    this.name = name;
    this.blobKey = blobKey;
    this.timestamp = timestamp;
    this.email = email;
  }
}