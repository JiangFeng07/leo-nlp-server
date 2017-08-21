package com.leo.nlp.algorithm;

/**
 * Created by lionel on 17/8/18.
 */
public class LongestCommonSubSequence implements StringDistance {

    public double getDistance(String s1, String s2) {
        return 1.0 * getLongestCommonSubSequence(s1, s2) / Math.max(s1.length(), s2.length());
    }

    private static int getLongestCommonSubSequence(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        }

        int lengthA = s1.length();
        int lengthB = s2.length();
        int[][] dist = new int[lengthA + 1][lengthB + 1];

        for (int i = lengthA - 1; i >= 0; i--) {
            for (int j = lengthB - 1; j >= 0; j--) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    dist[i][j] = dist[i + 1][j + 1] + 1;
                } else {
                    dist[i][j] = Math.max(dist[i + 1][j], dist[i][j + 1]);
                }
            }
        }
        return dist[0][0];
    }
}
