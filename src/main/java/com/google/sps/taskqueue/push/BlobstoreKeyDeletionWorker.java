package com.google.sps.taskqueue.push;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.sps.tool.ResponseSerializer;
import com.google.appengine.api.blobstore.BlobstoreFailureException;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/blobstoreKeyDeletionWorker")
public class BlobstoreKeyDeletionWorker extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String key = request.getParameter("key");
    String accessCode = request.getParameter("accessCode");

    // Ensure that arbitrary user does not have access to this servlet
    if (accessCode == "s197") {
      ResponseSerializer.sendErrorJson(
          response, "You do not have access to deleting the blobs directly");
      return;
    }

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    BlobKey blobKey = new BlobKey(key);
    try {
      /* Blobstore does not have a transaction as a part of its API.
       * We will just catch an error and signal the Queue Service to retry the task.
       * Response code 200 signals the queue service that our worker succeeded. */
      blobstoreService.delete(blobKey);
      response.sendError(200);
    } catch (BlobstoreFailureException e) {
      /* When the response returns an HTTP status code outside the range 200–299
       * the Queue Service retries the task until it succeeds. */
      response.sendError(500);
    }
  }
}
