package com.leo.nlp.utils;

/**
 * Created by lionel on 17/9/12.
 */
public class VectorUtil {
    /**
     * 求向量 A 和向量 B 的内积
     *
     * @param arrayA 向量 A
     * @param arrayB 向量 B
     * @return 向量 A 和向量 B 的内积
     */
    public static long arrayMultiply(long[] arrayA, long[] arrayB) {
        int length = arrayA.length;
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += arrayA[i] * arrayB[i];
        }
        return sum;
    }

    /**
     * 求向量的模
     *
     * @param array 数组向量
     * @return 向量的模
     */
    public static double vectorNorm(long[] array) {
        int sum = 0;
        for (long element : array) {
            sum += element * element;
        }
        return Math.sqrt(sum);
    }
}
