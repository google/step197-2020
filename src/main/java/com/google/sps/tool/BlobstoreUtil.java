package com.google.sps.tool;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.io.IOException;

public class BlobstoreUtil {

  private static final int DEFAULT_NUM_RETRIES = 5;

  public static void deleteBlobWithRetries(String blobKey) throws IOException {
    deleteBlobWithRetries(blobKey, DEFAULT_NUM_RETRIES);
  }

  public static void deleteBlobWithRetries(String blobKey, int retries) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    BlobKey key = new BlobKey(blobKey);

    while (retries != 0) {
      try {
        blobstoreService.delete(key);
        break;
      } catch (BlobstoreFailureException e) {
        --retries;
      }
    }

    if (retries == 0) {
      addBlobstoreDeleteTaskToQueue(key.getKeyString());
    }
  }

  public static void addBlobstoreDeleteTaskToQueue(String key) {
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(
        TaskOptions.Builder.withUrl("/blobstoreKeyDeletionWorker")
            .param("key", key)
            .param("accessCode", "s197"));
  }
}
