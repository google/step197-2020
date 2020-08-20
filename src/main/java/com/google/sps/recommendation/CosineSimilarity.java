/*package com.google.sps.recommendation;

import static java.lang.Math.sqrt;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class CosineSimilarity {

  public static double calculateCosineSimilarity(INDArray vectorA, INDArray vectorB) {
    System.out.println(vectorB);
    double dotProduct = vectorA.muli(vectorB).sumNumber().doubleValue();
    System.out.println(dotProduct);
    double magnitudeOfVectorA = calculateMagnitude(vectorA);
    double magnitudeOfVectorB = calculateMagnitude(vectorB);

    if (magnitudeOfVectorA != 0.0 || magnitudeOfVectorB != 0.0) {
        return dotProduct / (magnitudeOfVectorA * magnitudeOfVectorB); // Calculate Cosine Similarity
    } else {
        return 0.0;
    }
  }

  public static double calculateMagnitude(INDArray vector) {
    vector = vector.muli(vector);
    double sumOfRow = vector.sumNumber().doubleValue();
    return sqrt(sumOfRow);
  }
}
*/
