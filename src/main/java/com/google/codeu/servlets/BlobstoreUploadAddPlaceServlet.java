package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/add_place_blobstore")
public class BlobstoreUploadAddPlaceServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        BlobstoreService blobstoreService =
                BlobstoreServiceFactory.getBlobstoreService();
        String uploadUrlAddPlace = blobstoreService.createUploadUrl("/api/place");

        response.setContentType("text/plain");
        response.getOutputStream().print(uploadUrlAddPlace);
    }
}
