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
import com.google.appengine.api.datastore.TransactionOptions.Builder;
import com.google.appengine.api.datastore.Transaction;

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
import org.joda.time.DateTimeUtils;

/**
 * Handles the study mode questions and responses by adjusting and sorting a card's based on their
 * familiarity score and last time seen.
 */
@WebServlet("/study")
public class StudyServlet extends HttpServlet {

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

  private int numOfCardsPerRound = 3;
  private int maxNumOfRounds = 4;

  public void setTestingNumOfCards(int numCards) {
    numOfCardsPerRound = numCards;
  }

  public void setTestingNumOfRounds(int numRounds) {
    maxNumOfRounds = numRounds;
  }

  // Returns QuizCard's that are sorted by familiarity score as json
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    List<Card> userCards = new ArrayList<>();
    // Handles a logged out user
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/StudyMode");
      return;
    }

    String folderKey = request.getParameter("folderKey");
    Query cardQuery = new Query("Card").setAncestor(KeyFactory.stringToKey(folderKey));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(cardQuery);

    if (results != null) {
      for (Entity entity : results.asIterable()) {
        // If the card has no familiarity score then it's set to the default value
        Entity updatedEntity = initializeCard(entity);
        Card storedCard = storeCard(updatedEntity);
        userCards.add(storedCard);
      }
    }
    // Sorts cards in increasing order of familiarity score
    userCards.sort(
        (Card c1, Card c2) -> Double.compare(c1.getFamiliarityScore(), c2.getFamiliarityScore()));
    List<List<QuizCard>> quiz = createQuizRounds(userCards);

    Gson gson = new Gson();
    String jsonResponse = gson.toJson(quiz);
    response.setContentType("application/json;");
    response.getWriter().println(jsonResponse);
  }

  // Updates and stores a card's new familiarity score
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity cardEntity;
    Double newScore;
    Boolean answeredCorrectly = Boolean.parseBoolean(request.getParameter("answeredCorrectly"));
    String cardKey = request.getParameter("cardKey");
    long time = DateTimeUtils.currentTimeMillis();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    try {
      cardEntity = datastore.get(KeyFactory.stringToKey(cardKey));
    } catch (EntityNotFoundException e) {
      return;
    }

    Card card = new Card(cardEntity);
    if (answeredCorrectly) {
      newScore = incFamiliarityScore(time, card.getFamiliarityScore(), card.getTimeTested());
    } else {
      newScore = decFamiliarityScore(time, card.getFamiliarityScore(), card.getTimeTested());
    }
    cardEntity.setProperty("familiarityScore", newScore);
    cardEntity.setProperty("timeTested", time);
    storeCard(cardEntity);
  }

  /**
   * Takes a list of cards that have been sorted and breaks them into smaller lists that represent
   * rounds.
   */
  private List<List<QuizCard>> createQuizRounds(List<Card> userCards) {
    List<List<QuizCard>> quiz = new ArrayList<List<QuizCard>>();
    int numRounds = userCards.size() / numOfCardsPerRound;
    int start = 0;
    if (numRounds > maxNumOfRounds) {
      numRounds = maxNumOfRounds;
    }

    for (int i = 0; i < numRounds; i++) {
      quiz.add(new ArrayList<QuizCard>());
      for (int j = start; j < (start + numOfCardsPerRound); j++) {
        // Calls constructor that fetches similar words for the quiz
        QuizCard quizCard = new QuizCard(userCards.get(j));
        quiz.get(i).add(quizCard);
      }
      start += numOfCardsPerRound;
    }

    return quiz;
  }

  private Entity initializeCard(Entity entity) {
    // If cards don't have a familiarity score then provide default value
    if (!entity.hasProperty("familiarityScore")) {
      entity.setProperty("familiarityScore", .5);
      entity.setProperty("timeTested", System.currentTimeMillis());
    }

    return entity;
  }

  private Card storeCard(Entity entity) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(entity);
    Card card = new Card(entity);
    return card;
  }

  private Double incFamiliarityScore(long time, Double currentScore, long prevTime) {
    Double numHours = (double) (time - prevTime) / 3600000;
    if (numHours > 168) {
      numHours = 168.0;
    }
    return (currentScore + ((numHours / 168) * 2.4)) + 1;
  }

  private Double decFamiliarityScore(long time, Double currentScore, long prevTime) {
    Double numHours = (double) (time - prevTime) / 3600000;
    if (numHours > 24) {
      numHours = 0.0;
    } else {
      numHours = (24 / (numHours)) / 10;
    }
    return (currentScore - ((numHours) + 1));
  }
}
