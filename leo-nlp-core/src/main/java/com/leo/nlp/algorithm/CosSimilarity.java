package com.leo.nlp.algorithm;

import com.leo.nlp.utils.VectorUtil;

/**
 * Created by lionel on 17/9/12.
 */
public class CosSimilarity {
    public static double cos(long[] arrayA, long[] arrayB) {
        if (arrayA == null || arrayA.length <= 0
                || arrayB == null || arrayB.length <= 0
                || arrayA.length != arrayB.length) {
            return 0.0;
        }
        if (VectorUtil.vectorNorm(arrayA) != 0 && VectorUtil.vectorNorm(arrayB) != 0) {
            return 1.0 * VectorUtil.arrayMultiply(arrayA, arrayB) / (VectorUtil.vectorNorm(arrayA) * VectorUtil.vectorNorm(arrayB));
        }
        return 0.0;
    }

    public static void main(String[] args) {
        long[] arrayA = new long[]{1, 1, 2, 1, 1, 1, 0, 0, 0};
        long[] arrayB = new long[]{1, 1, 1, 0, 1, 1, 1, 1, 1};
        System.out.println(cos(arrayA, arrayB));
    }
}
