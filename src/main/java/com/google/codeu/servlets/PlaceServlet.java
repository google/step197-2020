
package com.google.codeu.servlets;

import com.google.api.Http;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.User;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.google.codeu.data.Place;

@WebServlet("/api/place")
public class PlaceServlet extends HttpServlet {
    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }


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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/");
            return;
        }

        String user = userService.getCurrentUser().getEmail();

        // Get the name entered by the user.
        String userNameText =
                Jsoup.clean(request.getParameter("name"), Whitelist.none());
        System.out.println(userNameText);

        // Get the description entered by the user.
        String userDescription =
                Jsoup.clean(request.getParameter("description"), Whitelist.none());
        System.out.println(userDescription);


        // Get the x_coord entered by the user.
        String userXCoordText =
                Jsoup.clean(request.getParameter("x_coord"), Whitelist.none());
        System.out.println(userXCoordText);

        // Get the y_coord entered by the user.
        String userYCoordText =
                Jsoup.clean(request.getParameter("y_coord"), Whitelist.none());
        System.out.println(userYCoordText);


        // Get the URL of the image that the user uploaded to Blobstore.
        String imageUrl = BlobstoreServlet.getUploadedFileUrl(request, "image");

        Place place = new Place(user, userNameText, userDescription, Double.parseDouble(userXCoordText), Double.parseDouble(userYCoordText));
        Place.store(place);
    }
}
