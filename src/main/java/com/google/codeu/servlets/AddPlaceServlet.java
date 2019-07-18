package com.google.codeu.servlets;

import com.google.api.Http;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@WebServlet("/api/place")
public class AddPlaceServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/");
            return;
        }

        String user = userService.getCurrentUser().getEmail();
        // Get the message entered by the user.
        String userXCoordText =
                Jsoup.clean(request.getParameter("x_coord"), Whitelist.none());


        // Get the URL of the image that the user uploaded to Blobstore.
        String imageUrl = BlobstoreServlet.getUploadedFileUrl(request, "image");
    }
}

