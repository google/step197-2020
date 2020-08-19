package com.google.sps.taskqueue.push;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobstoreFailureException;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/blobstoreWorker")
public class BlobstoreTaskWorker extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String key = request.getParameter("key");

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    BlobKey blobKey = new BlobKey(key);
    try {
      // Blobstore doesn't have a transaction as a part of its API
      // We will just catch an error and signal
      // the Queue to retry the task
      blobstoreService.delete(blobKey);
    } catch (BlobstoreFailureException e) {
      // When the response returns an HTTP status code
      // outside the range 200–299
      // the queue retries the task until it succeeds.
      response.sendError(500);
    }
  }
}
