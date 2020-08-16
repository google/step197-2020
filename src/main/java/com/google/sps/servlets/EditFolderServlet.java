package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.sps.tool.ResponseParser;
import com.google.gson.Gson;

@WebServlet("/editfolder")
public class EditFolderServlet extends HttpServlet {

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String newFolderName = request.getParameter("folderName");
      String newFolderDefaultLanguage = request.getParameter("folderDefaultLanguage");
      String folderKey = request.getParameter("folderKey");

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Entity folderEntity = getExistingFolderInDatastore(response, datastore, folderKey);

      if (folderEntity == null) {
        String jsonErrorInfo = ResponseParser.getErrorJson("Cannot edit Folder at the moment");
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      } else {
        updateFolder(
            response, datastore, folderEntity, newFolderName, newFolderDefaultLanguage, folderKey);
      }
    }
  }

  private void updateFolder(
      HttpServletResponse response,
      DatastoreService datastore,
      Entity folderEntity,
      String newFolderName,
      String newFolderDefaultLanguage,
      String folderKey)
      throws IOException {

    folderEntity.setProperty("folderName", newFolderName);
    folderEntity.setProperty("folderDefaultLanguage", newFolderDefaultLanguage);

    datastore.put(folderEntity);
  }

  private Entity getExistingFolderInDatastore(
      HttpServletResponse response, DatastoreService datastore, String folderKey)
      throws IOException {
    try {
      return datastore.get(KeyFactory.stringToKey(folderKey));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
}
