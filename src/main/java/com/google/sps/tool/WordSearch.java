package com.google.sps.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

public class WordSearch {
  private static HashMap<Character, ArrayList<String>> alphabetWordMap =
      new HashMap<Character, ArrayList<String>>();

  public static ArrayList<String> generateWordOptions(String correctAnswer) {
    ArrayList<String> quizWords = new ArrayList<>();
    quizWords.add(correctAnswer);

    if (!alphabetWordMap.containsKey('a') && !initMap()) {
      // If the dictionary file can't be read then it just return the correct Answer
      return quizWords;
    }

    char firstLetterOfAnswer = correctAnswer.charAt(0);
    ArrayList<String> wordList = alphabetWordMap.get(Character.toLowerCase(firstLetterOfAnswer));
    Boolean wordFound = false;

    for (int i = 0; i < wordList.size(); i++) {
      if (wordList.get(i).equals(correctAnswer)) {
        wordFound = true;
        getRandomWords(i, wordList, quizWords);
        break;
      }
    }

    // If exact word could not be found, then generate random words with the same first letter
    if (!wordFound) {
      Random random = new Random();
      int count = 0;
      while (count < 3) {
        int index = random.nextInt(wordList.size());
        if (!quizWords.contains(wordList.get(index))) {
          quizWords.add(wordList.get(index));
          count++;
        }
      }
    }

    Collections.shuffle(quizWords);
    return quizWords;
  }

  private static void getRandomWords(
      Integer origin, ArrayList<String> wordList, ArrayList<String> quizWords) {
    Random random = new Random();
    int count = 0;
    while (count < 3) {
      int index = origin + random.nextInt(20) - 10;
      index = Math.min(Math.max(index, 0), wordList.size() - 1);
      String possibleWord = wordList.get(index);
      // Ensures that we don't have repeating words
      if (!quizWords.contains(possibleWord)) {
        quizWords.add(possibleWord);
        count++;
      }
    }
  }

  /**
   * Goes through a text file of words in the English dictionary and creates a hashmap where the key
   * is a letter of the alphabet and the value is a list of words that begin with that letter.
   */
  private static Boolean initMap() {
    int retries = 5;
    while (retries > 0) {
      try {
        File dictionary = new File("./WEB-INF/classes/META-INF/Dictionary.txt");
        dictionary.setReadable(true);
        Scanner reader = new Scanner(dictionary);

        while (reader.hasNextLine()) {
          String word = reader.nextLine();
          char firstLetter = word.charAt(0);
          if (!alphabetWordMap.containsKey(firstLetter)) {
            alphabetWordMap.put(firstLetter, new ArrayList<String>());
          }
          alphabetWordMap.get(firstLetter).add(word);
        }

        reader.close();
        return true;
      } catch (FileNotFoundException e) {
        retries--;
        alphabetWordMap.clear();
      }
    }
    return false;
  }
}
