/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.Entity;
import java.util.UUID;

/** A single message posted by a user. */
public class Place {

  private UUID id;
  private String owner;
  private String title;
  private String description;
  private long latitude;
  private long longitude;
  private long timestamp;

  /**
   * Constructs a new {@link Place} posted by {@code user} with {@code text}
   * content. Generates a random ID and uses the current system time for the
   * creation time.
   */
  public Place(String owner, String title, String description, long latitude, long longitude) {
    this(UUID.randomUUID(), owner, title, description, latitude, longitude, System.currentTimeMillis());
  }

  public Place(UUID id, String owner, String title, String description, long latitude, long longitude, long timestamp) {
    this.id = id;
    this.owner = owner;
    this.title = title;
    this.description = description;
    this.latitude = latitude;
    this.longitude = longitude;
    this.timestamp = timestamp;
  }

  
  /** Return Place data using based on entity from search query. */
  public Place(Entity entity) {
    this.id = UUID.fromString((String)entity.getProperty("id"));
    this.owner = (String)entity.getProperty("owner");
    this.title = (String)entity.getProperty("title");
    this.description = (String)entity.getProperty("description");
    this.latitude = (long)entity.getProperty("latitude");
    this.longitude = (long)entity.getProperty("longitude");
    this.timestamp = (long)entity.getProperty("timestamp");
  }

  public UUID getId() {
    return id;
  }

  public String getOwner() {
    return owner;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public long getLatitude() {
    return latitude;
  }

  public long getLongitude() {
    return longitude;
  }

  public long getTimestamp() {
    return timestamp;
  }

}
