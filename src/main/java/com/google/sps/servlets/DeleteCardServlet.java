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
import com.google.sps.tool.ResponseSerializer;
import com.google.sps.data.Card;
import com.google.sps.tool.BlobstoreUtil;

@WebServlet("/deletecard")
public class DeleteCardServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      ResponseSerializer.sendErrorJson(response, "User not logged in");
      return;
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String cardKey = request.getParameter("cardKey");
    Entity card = getExistingCardInDatastore(datastore, cardKey);

    if (card == null) {
      ResponseSerializer.sendErrorJson(response, "Cannot edit Card");
      return;
    } else {
      String imageBlobKey = (String) card.getProperty("imageBlobKey");
      if (imageBlobKey != null) {
        BlobstoreUtil.deleteBlobWithRetries(imageBlobKey);
      }
      Card.deleteCardWithRetries(card);
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
}
