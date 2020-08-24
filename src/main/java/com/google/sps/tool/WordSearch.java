package com.google.sps.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Collections;
import java.util.Random;

public class WordSearch {
  private static HashMap<Character, ArrayList<String>> firstLetterMap =
      new HashMap<Character, ArrayList<String>>();

  public static ArrayList<String> generateWordOptions(String correctAnswer) {
    ArrayList<String> quizWords = new ArrayList<>();
    quizWords.add(correctAnswer);

    if (!firstLetterMap.containsKey('a')) {
      // If hashmap can't read file then it should just return the correct Answer
      if(!initMap()) {
        return quizWords;
      }
    }

    char firstLetter = correctAnswer.charAt(0);
    ArrayList<String> wordList = firstLetterMap.get(Character.toLowerCase(firstLetter));
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
      for (int i = 0; i < 3; i++) {
        int index = random.nextInt(wordList.size());
        quizWords.add(wordList.get(index));
      }
    }

    Collections.shuffle(quizWords);
    return quizWords;
  }

  private static void getRandomWords(
      Integer origin, ArrayList<String> wordList, ArrayList<String> quizWords) {
    Random random = new Random();
    for (int i = 0; i < 3; i++) {
      try {
        int index = random.nextInt(20) - 10;
        String possibleWord = wordList.get(origin + index);
        // Ensures that we don't have repeating words
        if (!quizWords.contains(possibleWord)) {
          quizWords.add(possibleWord);
        } else {
          i--;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        // Trying again if we get a word thats out of bounds
        i--;
      }
    }
  }

  /**
   * Goes through  a text file of words in the english dictionary and creates
   * a hashmap where the key is a letter of the alphabet and the value is a list
   * of words that begin with that letter.
   */
  private static Boolean initMap() {
    int retries = 0;
    try {
      File dictionary = new File("./WEB-INF/classes/META-INF/Dictionary.txt");
      dictionary.setReadable(true);
      Scanner reader = new Scanner(dictionary);

      while (reader.hasNextLine()) {
        String word = reader.nextLine();
        char firstLetter = word.charAt(0);
        if (!firstLetterMap.containsKey(firstLetter)) {
          firstLetterMap.put(firstLetter, new ArrayList<String>());
        }
        firstLetterMap.get(firstLetter).add(word);
      }

      reader.close();
    } catch (FileNotFoundException e) {
      if(retries > 5) {
        return false;
      }
      // If parsing fails try again
      firstLetterMap.clear();
      initMap();
    }
    return true;
  }

}