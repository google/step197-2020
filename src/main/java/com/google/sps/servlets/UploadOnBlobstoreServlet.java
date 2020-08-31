package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/upload")
public class UploadOnBlobstoreServlet extends HttpServlet {

  // Create an upload URL for the user to upload their images to BlobStore
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    // Redirects the user to /usercards servlet upon completion of image being processed
    String uploadUrl = blobstoreService.createUploadUrl("/usercards");

    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);
  }
}
