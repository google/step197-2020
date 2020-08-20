package com.google.sps.recommendation;

public class Pair {

    double cosineSimilarity;
    String word;
    int id;

    public Pair(double cosineSimilarity, int id) {
        this.cosineSimilarity = cosineSimilarity;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public double getCosineSimilarity() {
        return this.cosineSimilarity;
    }

    public String getWord() {
        return this.word;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }
}