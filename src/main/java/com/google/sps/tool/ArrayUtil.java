package com.google.sps.tool;

import java.util.Comparator;
import java.util.Arrays;

public class ArrayUtil {

  // Java implementation of Numpy's argsort
  // https://numpy.org/doc/stable/reference/generated/numpy.argsort.html
  public static int[] argsort(float[] a) {
    return argsort(a, true);
  }

  public static int[] argsort(final float[] a, final boolean ascending) {
    Integer[] indexes = new Integer[a.length];
    for (int i = 0; i < indexes.length; i++) {
      indexes[i] = i;
    }
    Arrays.sort(
        indexes,
        new Comparator<Integer>() {
          @Override
          public int compare(final Integer i1, final Integer i2) {
            return (ascending ? 1 : -1) * Float.compare(a[i1], a[i2]);
          }
        });

    int[] ret = new int[indexes.length];
    for (int i = 0; i < ret.length; i++) ret[i] = (int) indexes[i];

    return ret;
  }
}
