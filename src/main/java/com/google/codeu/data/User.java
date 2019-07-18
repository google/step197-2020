package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.util.ArrayList;
import java.util.List;

public class User {

  private String email;
  private String aboutMe;
  private transient String id;

  public User(com.google.appengine.api.users.User user) {
    this.email = user.getEmail();
    this.aboutMe = "";
    this.id = user.getUserId();
  }

  public User(Entity entity) {
    this.email = (String) entity.getProperty("email");
    this.aboutMe = (String) entity.getProperty("aboutMe");
    this.id = (String) entity.getProperty("id");
  }

  public String getEmail() {
    return email;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public String getId() {
    return id;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public static List<User> getAll() {
    DatastoreService datastore = Datastore.GetSingletonService();
    Query query = new Query("User");
    PreparedQuery results = datastore.prepare(query);
    List<User> users = new ArrayList<User>();
    for (Entity entity : results.asIterable()) {
      users.add(new User(entity));
    }
    ;

    return users;
  }

  public static void store(User user) {
    DatastoreService datastore = Datastore.GetSingletonService();

    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    userEntity.setProperty("id", user.getId());
    datastore.put(userEntity);
  }

  public static User getByEmail(String email) {
    DatastoreService datastore = Datastore.GetSingletonService();

    Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
    Query query = new Query("User").setFilter(emailFilter);

    Entity entity = datastore.prepare(query).asSingleEntity();
    if (entity == null) {
      return null;
    }

    return new User(entity);
  }
}
