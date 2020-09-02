package com.google.sps.taskqueue.push;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.tool.ResponseSerializer;

@WebServlet("/datastoreEntityDeletionWorker")
public class DatastoreEntityDeletionWorker extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String key = request.getParameter("key");
    String accessCode = request.getParameter("accessCode");

    // Ensure that arbitrary user does not have access to this servlet
    if (accessCode == "s197") {
      ResponseSerializer.sendErrorJson(
          response, "You do not have access to deleting the entity directly");
      return;
    }

    try {
      // Response code 200 signals the queue service that our worker succeeded.
      deleteEntity(key);
      response.sendError(200);
    } catch (Exception e) {
      /* When the response returns an HTTP status code outside the range 200–299,
       * the Queue Service retries the task until it succeeds. */
      response.sendError(500);
    }
  }

  private void deleteEntity(String key) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      datastore.delete(KeyFactory.stringToKey(key));
    } catch (Exception e) {
      throw e;
    }
  }
}
