package com.google.sps.recommendation;

import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.reduce3.CosineSimilarity;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Main {
  public static void main(String[] args) throws IOException {
    WordVectors vec =
        WordVectorSerializer.readWord2VecModel(
            "/home/ngothomas/GoogleNews-vectors-negative300-SLIM.bin.gz");
    System.out.println("MODEL LOADED");
    InMemoryLookupTable lookupTable = (InMemoryLookupTable) vec.lookupTable();
    VocabCache lookupCache = (VocabCache) vec.vocab();

    Map<Integer, String> idToWord = new HashMap<>();
    Map<String, Integer> wordToId = new HashMap<>();
    Map<String, List<Pair>> index = new HashMap<>();

    // Construct word to id (vice versa) index table
    int counter = 0;
    for (Object i : lookupCache.words()) {
      String word = (String) i;
      idToWord.put(counter, word);
      wordToId.put(word,counter);
      counter++;
    }
    System.out.println("Construction of HashTable Finished...");

    for (Object i : lookupCache.words()) {
      long nanoOuterLoop = System.nanoTime();
      List<Pair> neighbors = new ArrayList<>();
      long nanoCos;
      long nanDone;
      for (Object j : lookupCache.words()) {
        INDArray wordi = (String) i;
        INDArray wordj = (String) j;
        if (wordi != wordj) {
          nanoCos = System.nanoTime();
          double cosineSimilarity = Transforms.cosineSim(d1, d2);
          nanDone = System.nanoTime();
    
          int idOfWordj = wordToId.get(wordj);

          Pair pair = new Pair(cosineSimilarity, idOfWordj);
          neighbors.add(pair);
    
        }
      }
  
      long nanoFinishedLoop = System.nanoTime();
      System.out.println("LOOP TIME:");
      System.out.println((nanoFinishedLoop-nanoOuterLoop)/1000000);
      // neighbors.sort(
      //     (Pair pair1, Pair pair2) -> Double.compare(pair2.getCosineSimilarity(), pair1.getCosineSimilarity()));
      int count=0;
      System.out.println("Done : " + count);
      count++;
      // index.put((String) i, neighbors);
    }
  }
}

