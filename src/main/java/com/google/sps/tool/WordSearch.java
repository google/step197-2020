package com.google.sps.tool;

import java.util.ArrayList;

public class WordSearch {
  public static ArrayList<String> generateWordOptions(String correctAnswer) {
    ArrayList<String> myList = new ArrayList<>();
    myList.add(correctAnswer);
    myList.add("test1");
    myList.add("test2");
    myList.add("test3");
    return myList;
  }
}
