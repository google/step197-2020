package com.google.sps.data;

/** A comment object. */
public final class Comment {

  private final long id;
  private final String name;
  private final String message;
  private final long timestamp;

  public Comment(long id, String name, String message, long timestamp) {
    this.id = id;
    this.name = name;
    this.message = name + ":     " + message;
    this.timestamp = timestamp;
  }
}