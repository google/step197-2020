package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD
@WebServlet("/blobstore-upload-url")
=======
@WebServlet("/api/blobstore-upload-url")
>>>>>>> 9d9e3571ca9958ec03ad478ea3ab971584ed306b
public class BlobstoreUploadUrlServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    BlobstoreService blobstoreService =
        BlobstoreServiceFactory.getBlobstoreService();
<<<<<<< HEAD
    String uploadUrl = blobstoreService.createUploadUrl("/my-form-handler");
=======
    String uploadUrl = blobstoreService.createUploadUrl("/api/blobstore_url");
>>>>>>> 9d9e3571ca9958ec03ad478ea3ab971584ed306b

    response.setContentType("text/plain");
    response.getOutputStream().print(uploadUrl);
  }
}
