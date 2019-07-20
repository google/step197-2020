
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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




/** A tag, which can map to many different places. */
public class Tag {

  private UUID id;
  private String label;
  
  /**
   * Constructs a new {@link Tag} with a label and a random ID
   */
  public Tag(String label) {
    this(UUID.randomUUID(), label);
  }

  /**
   * Constructs a new {@link Tag} posted using only a label
   */
  public Tag(UUID id, String label) {
    this.id = id;
    this.label = label;
  }

  /** Return Tag data using based on entity from search query. */
  public Tag(Entity entity) {
    this.label = (String)entity.getProperty("label");
    this.id = UUID.fromString(entity.getProperty("id"));
  }  
  
  public static List<Tag> getAll() {
    DatastoreService datastore = Datastore.GetSingletonService();
    Query query = new Query("Tag");
    PreparedQuery results = datastore.prepare(query);
    List<Tag> tags = new ArrayList<Tag>();
    for (Entity entity : results.asIterable()) {
      tags.add(new Tag(entity));
    }
    return tags;
  }

  
  public static Tag getByLabel(String label) {
    DatastoreService datastore = Datastore.GetSingletonService();
    Filter tagFilter = new FilterPredicate("label", FilterOperator.EQUAL, label);
    Query query = new Query("Tag").setFilter(tagFilter);
    Entity entity = datastore.prepare(query).asSingleEntity();
    
    if (entity == null) {
      return null;
    }
    return new Tag(entity);
  }

  public String getLabel() {
    return label;
  }
  
  public UUID getId() {
    return id;
  }

}
