package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.tool.ResponseSerializer;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.nio.file.Path;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.BTreeMap;
import org.mapdb.Serializer;

import com.google.gson.Gson;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      ResponseSerializer.sendErrorJson(response, "User not logged in");
      return;
    }

    String queryWord = request.getParameter("queryWord").toLowerCase();
    int numOfWordsRequested = Integer.parseInt(request.getParameter("numOfWordsRequested"));
    if (checkforWordRequestedBound(response, numOfWordsRequested)) {
      ResponseSerializer.sendErrorJson(response, "Number of words requested reaches query limit");
      return;
    }

    // Ensures db is opened in read only to avoid data perturbation
    DB db = DBMaker.fileDB(getFile()).readOnly().make();
    BTreeMap<String, String[]> queryNearestNeighbors = getIndexTable(db);

    try {
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
    } catch (NullPointerException e) {
      ResponseSerializer.sendErrorJson(response, "Cannot find similar words");
      return;
    }
    db.close();
  }

  private File getFile() {
    Storage storage = StorageOptions.newBuilder().setProjectId("framecards").build().getService();
    Blob blob = storage.get(BlobId.of("framecards.appspot.com", "word2vec.db"));

    try {
      File file = new File("/tmp/word2vec.db");
      // Creates the file and downloads its content if it does not exist
      if (file.createNewFile()) {
        blob.downloadTo(file.toPath());
      }
      return file;
    } catch (IOException e) {
      return null;
    }
  }

  private boolean checkforWordRequestedBound(HttpServletResponse response, int numOfWordsRequested)
      throws IOException {
    if (numOfWordsRequested > 49 || numOfWordsRequested < 1) {
      return true;
    }
    return false;
  }

  private BTreeMap<String, String[]> getIndexTable(DB db) {
    return db.treeMap("Main")
        .keySerializer(Serializer.STRING)
        .valueSerializer(Serializer.JAVA)
        .createOrOpen();
  }
}
