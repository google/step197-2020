package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.sps.tool.ResponseSerializer;
import com.google.sps.data.Card;

@WebServlet("/deletecard")
public class DeleteCardServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      String jsonErrorInfo = ResponseSerializer.getErrorJson("User not logged in");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      return;
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String cardKey = request.getParameter("cardKey");
    Entity card = getExistingCardInDatastore(datastore, cardKey);

    if (card == null) {
      String jsonErrorInfo = ResponseSerializer.getErrorJson("Cannot delete Card at the moment");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
    } else {
      String imageBlobKey = (String) card.getProperty("imageBlobKey");
      Card.deleteBlob(imageBlobKey);
      deleteCardWithRetries(card);
    }
  }

  private Entity getExistingCardInDatastore(DatastoreService datastore, String cardKey)
      throws IOException {
    try {
      return datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  private void deleteCardWithRetries(Entity card) {
    int retries = 5;
    while (true) {
      try {
        Card.deleteCard(card);
        break;
      } catch (Exception e) {
        if (retries == 0) {
          Card.addDatastoreDeleteTaskToQueue(card);
          break;
        }
        --retries;
      }
    }
  }
}
