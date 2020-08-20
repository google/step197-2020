/*package com.google.sps.recommendation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static java.lang.Math.sqrt;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CosineSimilarityTest {

  @Test
  public void calculateMagnitudeTestOne() {
    INDArray response = Nd4j.zeros(1, 2).addi(5); // Creates a 1x2 array <5,5>
    double actual = CosineSimilarity.calculateMagnitude(response);
    assertTrue(sqrt(50.0) == actual);
  }

  @Test
  public void calculateMagnitudeTestTwo() {
    INDArray response = Nd4j.zeros(1, 2).addi(7); // Creates a 1x2 array <7,7>
    double actual = CosineSimilarity.calculateMagnitude(response);
    assertTrue(sqrt(98) == actual);
  }
}
*/
