package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.tool.ResponseSerializer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;

import org.mapdb.*;
import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
  private DB db;
  private BTreeMap<String, String[]> queryNearestNeighbors;

  @Override
  public void init() {
    String path = Paths.get("/home/ngothomas/downloads/webapp/step197-2020/word2vec.db").toString();
    db = DBMaker.fileDB(path).make();
    queryNearestNeighbors =
        db.treeMap("Main")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.JAVA)
            .createOrOpen();
  }

  @Override
  public void destroy() {
    db.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      String jsonErrorInfo = ResponseSerializer.getErrorJson("User not logged in");
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(jsonErrorInfo));
      return;
    }

    String queryWord = request.getParameter("queryWord");
    int numOfWordsRequested = Integer.parseInt(request.getParameter("numOfWordsRequested"));

    String[] neighbors = queryNearestNeighbors.get(queryWord);

    // We skip neighbors[0] because that is the same word as "queryWord"
    String[] requestedNeighbors = new String[numOfWordsRequested];
    for (int i = 1; i < numOfWordsRequested + 1; i++) {
      requestedNeighbors[i - 1] = neighbors[i];
    }

    Map<String, String[]> jsonInfo = new HashMap<>();
    jsonInfo.put(queryWord, requestedNeighbors);
    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(jsonInfo));
  }
}
