package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.gson.Gson;

import com.google.sps.data.Card;
import com.google.sps.tool.WordSearch;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Collections;

/** * */
@WebServlet("/study")
public class StudyServelet extends HttpServlet {
  public class QuizCard {
    private String quizWord;
    private String cardKey;
    private ArrayList<String> possibleResponses = new ArrayList<>();
    private String correctAnswer;

    public QuizCard(Card card) {
      this.quizWord = card.getTextTranslated();
      this.cardKey = card.getCardKey();
      this.correctAnswer = card.getRawText().toLowerCase();
      this.possibleResponses = WordSearch.generateWordOptions(this.correctAnswer);
    }
  }

  class SortByFamilarity implements Comparator<Card> {
    public int compare(Card a, Card b) {
      return (int) (a.getFamilarityScore() - b.getFamilarityScore());
    }
  }

  private int numOfCardsPerRound = 2;
  private int maxNumOfRounds = 4;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    List<Card> userCards = new ArrayList<>();

    if (userService.isUserLoggedIn()) {
      String folderKey = request.getParameter("folderKey");
      Query cardQuery = new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey));
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(cardQuery);

      if (results != null) {
        for (Entity entity : results.asIterable()) {
          userCards.add(initializeCard(entity));
        }
      }

      Collections.sort(userCards, new SortByFamilarity());
      List<List<QuizCard>> quiz = createQuizRounds(userCards);
      Gson gson = new Gson();
      String jsonResponse = gson.toJson(quiz);

      response.setContentType("application/json;");
      response.getWriter().println(jsonResponse);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity cardEntity;
    Double newScore;
    String answeredCorrectly = request.getParameter("answeredCorrectly");
    String cardKey = request.getParameter("cardKey");
    long time = System.currentTimeMillis();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    try {
      cardEntity = datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      cardEntity = null;
    }

    if (cardEntity != null) {
      Card card = new Card(cardEntity);
      if (answeredCorrectly.equals("true")) {
        newScore = incFamilarityScore(time, card.getFamilarityScore(), card.getTimeTested());
      } else {
        newScore = decFamilarityScore(time, card.getFamilarityScore(), card.getTimeTested());
      }
      cardEntity.setProperty("familarityScore", newScore);
      cardEntity.setProperty("timeTested", time);
      datastore.put(cardEntity);
    }
  }

  private List<List<QuizCard>> createQuizRounds(List<Card> userCards) {
    List<List<QuizCard>> quiz = new ArrayList<List<QuizCard>>();
    List<QuizCard> holder = new ArrayList<QuizCard>();
    int numRounds = userCards.size() / numOfCardsPerRound;
    int start = 0;
    if (numRounds > maxNumOfRounds) {
      numRounds = maxNumOfRounds;
    }

    for (int i = 0; i < numRounds; i++) {
      quiz.add(new ArrayList<QuizCard>());
      for (int j = start; j < (start + numOfCardsPerRound); j++) {
        QuizCard quizCard = new QuizCard(userCards.get(j));
        quiz.get(i).add(quizCard);
      }
      start += numOfCardsPerRound;
    }

    return quiz;
  }

  private Card initializeCard(Entity entity) {
    // If cards don't have a familarity score then provide default
    if (!entity.hasProperty("familarityScore")) {
      entity.setProperty("familarityScore", .5);
      entity.setProperty("timeTested", System.currentTimeMillis());
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(entity);
    }
    Card card = new Card(entity);
    return card;
  }

  private Double incFamilarityScore(long time, Double currentScore, long prevTime) {
    Double numHours = (double) (time - prevTime) / 3600;
    if (numHours > 168) {
      numHours = 168.0;
    }
    return (currentScore + ((numHours / 168) * 2.4)) + 1;
  }

  private Double decFamilarityScore(long time, Double currentScore, long prevTime) {
    Double numHours = (double) (time - prevTime) / 3600;
    if (numHours > 24) {
      numHours = 0.0;
    } else {
      numHours = (24 / (numHours)) / 10;
    }
    return (currentScore - ((numHours) + 1));
  }
}
