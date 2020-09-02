package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.KeyFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.sps.data.Folder;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/userfolders")
public class UserFoldersServlet extends HttpServlet {

  private static final String CREATE_FORM_KEY = "showCreateFormStatus";
  private static final String USER_FOLDER_KEY = "userFolders";

  /*
   * Query all folders from current user
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    Map<String, Object> jsonInfo = new HashMap<>();
    List<Folder> userFolders = new ArrayList<>();
    jsonInfo.put(CREATE_FORM_KEY, false);

    if (userService.isUserLoggedIn()) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      String userKey = getUserKey(userService, datastore);

      Query folderQuery = new Query("Folder").setAncestor(KeyFactory.stringToKey(userKey));
      PreparedQuery results = datastore.prepare(folderQuery);

      if (results != null) {
        for (Entity entity : results.asIterable()) {
          if ((boolean) entity.getProperty("deleted") == true) {
            continue;
          }
          Folder folder = new Folder(entity);
          userFolders.add(folder);
        }
      }

      jsonInfo.put(CREATE_FORM_KEY, true);
    }

    jsonInfo.put(USER_FOLDER_KEY, userFolders);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonInfo));
  }

  /*
   * Store new folder into Datastore
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      String folderName = request.getParameter("folderName");
      String folderDefaultLanguage = request.getParameter("folderDefaultLanguage");
      String userKey = getUserKey(userService, datastore);

      Folder folder = new Folder(folderName, folderDefaultLanguage);
      folder.setParentKey(userKey);

      Entity folderEntity = folder.createEntity();

      datastore.put(folderEntity);
    }
    response.sendRedirect("/MyFolders");
  }

  public String getUserKey(UserService userService, DatastoreService datastore) {
    String userEmail = userService.getCurrentUser().getEmail();
    Query userQuery =
        new Query("User").setFilter(new FilterPredicate("email", FilterOperator.EQUAL, userEmail));
    Entity userEntity = datastore.prepare(userQuery).asSingleEntity();
    String userKey = KeyFactory.keyToString(userEntity.getKey());

    return userKey;
  }
}
