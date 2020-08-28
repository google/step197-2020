package com.google.sps.tool;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class ArrayUtilTest {

  @Test
  public void testArgsort() {
    float[] sample = new float[] {(float) 0.6, (float) 0.2, (float) 0.5};
    int[] response = ArrayUtil.argsort(sample);
    int[] expected = new int[] {1, 2, 0};
    assertTrue(Arrays.equals(response, expected));
  }
}
