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
      initMap();
    }
    char firstLetter = correctAnswer.charAt(0);
    ArrayList<String> prefixList = firstLetterMap.get(Character.toLowerCase(firstLetter));
    Boolean wordFound = false;

    for (int i = 0; i < prefixList.size(); i++) {
      if (prefixList.get(i).equals(correctAnswer)) {
        wordFound = true;
        getRandomWords(i, prefixList, quizWords);
        break;
      }
    }

    // If we could not find the exact word, then we will generate random words based on the first
    // letter
    if (!wordFound) {
      Random random = new Random();
      for (int i = 0; i < 3; i++) {
        int index = random.nextInt(prefixList.size());
        quizWords.add(prefixList.get(index));
      }
    }

    Collections.shuffle(quizWords);
    return quizWords;
  }

  private static void getRandomWords(
      Integer origin, ArrayList<String> prefixList, ArrayList<String> quizWords) {
    Random random = new Random();
    for (int i = 0; i < 3; i++) {
      try {
        int index = random.nextInt(20) - 10;
        String possibleWord = prefixList.get(origin + index);
        // Ensures that we don't have repeating words
        if (!quizWords.contains(possibleWord)) {
          quizWords.add(possibleWord);
        } else {
          i--;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        // Trying again if we get a word that out of bounds
        i--;
      }
    }
  }

  private static void initMap() {
    try {
      File dictionary = new File("./WEB-INF/classes/META-INF/Dictionary.txt");
      System.out.println(dictionary.getAbsolutePath());
      dictionary.setReadable(true);
      System.out.println(dictionary.exists());
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
      // If parsing fails try again
      firstLetterMap.clear();
    }
  }
}
