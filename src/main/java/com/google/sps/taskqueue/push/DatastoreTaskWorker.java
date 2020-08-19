package com.google.sps.taskqueue.push;

import static com.google.appengine.api.datastore.TransactionOptions.Builder.*;

import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/datastoreWorker")
public class DatastoreTaskWorker extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String key = request.getParameter("key");

    try {
      deleteEntity(key);
    } catch (Exception e) {
      // When the response returns an HTTP status code
      // outside the range 200–299
      // the queue retries the task until it succeeds.
      response.sendError(500);
    }
  }

  private void deleteEntity(String key) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction(withXG(true));
    try {
      datastore.delete(KeyFactory.stringToKey(key));
      txn.commit();
    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }
  }
}
