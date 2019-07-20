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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;

/** Class that maps tag-place relationships. */
public class PlaceTag {

  private Key place;
  private Key tag;

  /**
   * Constructs a new {@link PlaceTag} posted using only references to a tag and
   * place
   */
  public PlaceTag(Key place, Key tag) {
    this.place = place;
    this.tag = tag;
  }

  /**
   * Constructs a new {@link PlaceTag} using a place and the tag
   */
  public PlaceTag(Place place, Tag tag) {
    this.place = KeyFactory.createKey("Place", place.getId().toString());
    this.tag = KeyFactory.createKey("Tag", tag.getId().toString());
  }

  /** Return PlaceTag data using based on entity from search query. */
  public PlaceTag(Entity entity) {
    // create key based on the string returned from getting the place and tag
    this.place = KeyFactory.createKey("Place", entity.getProperty("place").toString());
    this.tag = KeyFactory.createKey("Tag", entity.getProperty("tag").toString());
  }

  /** Stores a new PlaceTag in Datastore. */
  public static void store(PlaceTag placeTag) {
    DatastoreService datastore = Datastore.GetSingletonService();
    Entity placeTagEntity = new Entity("PlaceTag",
        KeyFactory.keyToString(placeTag.getPlace()) + KeyFactory.keyToString(placeTag.getTag()));
    placeTagEntity.setProperty("place", placeTag.getPlace());
    placeTagEntity.setProperty("tag", placeTag.getTag());

    datastore.put(placeTagEntity);
  }

  /** Get all the PlaceTags currently in the Datastore. */
  public static List<PlaceTag> getAll() {
    DatastoreService datastore = Datastore.GetSingletonService();
    List<PlaceTag> placeTags = new ArrayList<>();
    Query query = new Query("PlaceTag").addSort("tag", SortDirection.DESCENDING).addSort("place",
        SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      placeTags.add(new PlaceTag(entity));
    }
    return placeTags;
  }

  /** Get all the PlaceTags who have the same tag currently in the Datastore. */
  public static List<PlaceTag> getByTag(Tag tag) {
    DatastoreService datastore = Datastore.GetSingletonService();
    List<PlaceTag> placeTags = new ArrayList<>();
    Key tagId = KeyFactory.createKey("Tag", tag.getId().toString());
    Query query = new Query("PlaceTag").setFilter(new Query.FilterPredicate("tag", FilterOperator.EQUAL, tagId))
        .addSort("place", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      placeTags.add(new PlaceTag(entity));
    }
    return placeTags;
  }

  public Key getPlace() {
    return place;
  }

  public Key getTag() {
    return tag;
  }

}
