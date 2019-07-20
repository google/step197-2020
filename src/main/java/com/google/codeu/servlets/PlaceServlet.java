package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.Place;
import com.google.codeu.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/api/place")
public class PlaceServlet extends HttpServlet {
  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with a JSON representation of {@link Message} data for a specific user. Responds with
   * an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String email = request.getParameter("user");
    List<Place> places;
    if (email == null || email.isEmpty()) {
      // Request is invalid, return empty array
      // esponse.getWriter().println("[]");
      places = Place.getAll();
    } else {
      User user = User.getByEmail(email);
      if (user == null) {
        response.setStatus(400);
        return;
      }
      places = Place.getByUser(User.getByEmail(email));
    }

    Gson gson = new Gson();
    String json = gson.toJson(places);

    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/");
      return;
    }

    String user = userService.getCurrentUser().getEmail();

    String userNameText = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String userDescription = Jsoup.clean(request.getParameter("description"), Whitelist.none());
    String userLongitudeText = Jsoup.clean(request.getParameter("lng"), Whitelist.none());
    String userLatitudeText = Jsoup.clean(request.getParameter("lat"), Whitelist.none());

    // Get the URL of the image that the user uploaded to Blobstore.
    String imageUrl = BlobstoreServlet.getUploadedFileUrl(request, "image");

    Place place =
        new Place(
            user,
            userNameText,
            userDescription,
            Double.parseDouble(userLatitudeText),
            Double.parseDouble(userLongitudeText));
    Place.store(place);
  }
}
