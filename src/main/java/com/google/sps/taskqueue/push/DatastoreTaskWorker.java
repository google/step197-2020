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

@WebServlet("/datastoreEntityDeletionWorker")
public class DatastoreTaskWorker extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String key = request.getParameter("key");
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
