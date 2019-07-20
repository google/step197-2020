package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.Place;
import com.google.codeu.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/api/place")
public class UserMapServlet extends HttpServlet {

  /**
   * Responds with a JSON representation of {@link Message} data for a specific
   * user. Responds with an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("application/json");

    String email = request.getParameter("user");
    List<Place> places;
    if (email == null || email.isEmpty()) {
      // Request is invalid, return empty array
      // esponse.getWriter().println("[]");
      places = Place.getAll();
    } else {
      User user = User.getByEmail(email);
      if(user == null){
        response.setStatus(400);
        return;
      }
      places = Place.getByUser(User.getByEmail(email));
    }

    Gson gson = new Gson();
    String json = gson.toJson(places);

    response.getWriter().println(json);
  }
}
