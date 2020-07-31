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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.sps.data.Folder;
import java.util.List; 
import java.util.ArrayList;


/** **/
@WebServlet("/userfolders")
public class UserFoldersServlet extends HttpServlet {

  /**
  * Query all folders from current user
  */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {

      List<Folder> userFolders = new ArrayList<>();

      String userKey = request.getParameter("userKey");

      // Query all folders identified by the userKey
      Query folderQuery = new Query("Folder").setAncestor(KeyFactory.stringToKey(userKey));

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(folderQuery);

      // Add folders to a list
      if (results != null) {
        for (Entity entity: results.asIterable()) {
          Folder folder = Folder.EntityToFolder(entity);
          userFolders.add(folder);
        }
      }
      
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(userFolders));
    }
  }
  
  /**
  ** Store new folder into Datastore
  **/

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {

    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      
      String folderName = request.getParameter("folderName");
      String folderDefaultLanguage = request.getParameter("folderDefaultLanguage");
      String userKey = request.getParameter("userKey");

      Folder folder = new Folder(folderName, folderDefaultLanguage);
      Entity folderEntity = folder.createEntity(KeyFactory.stringToKey(userKey));
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(folderEntity);
    }
  } 
}