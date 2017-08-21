package com.leo.nlp.algorithm;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lionel on 17/8/18.
 */
public class LongestCommonSubString implements StringDistance {
    public double getDistance(String s1, String s2) {
        return 1.0 * getLongestCommonSubString(s1, s2) / Math.max(s1.length(), s2.length());
    }

    private int getLongestCommonSubString(String s1, String s2) {
        int maxLength = 0;
        if (StringUtils.isBlank(s1) || StringUtils.isBlank(s2)) {
            return maxLength;
        }

        int lengthA = s1.length();
        int lengthB = s2.length();
        int[][] dist = new int[lengthA][lengthB];
        for (int i = 0; i < lengthA; i++) {
            if (s1.charAt(i) == s2.charAt(0)) {
                dist[i][0] = 1;
            } else {
                dist[i][0] = 0;
            }
        }
        for (int j = 0; j < lengthB; j++) {
            if (s1.charAt(0) == s2.charAt(j)) {
                dist[0][j] = 1;
            } else {
                dist[0][j] = 0;
            }
        }
        for (int i = 1; i < lengthA; i++) {
            for (int j = 1; j < lengthB; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    dist[i][j] = dist[i - 1][j - 1] + 1;
                } else
                    dist[i][j] = 0;
            }
        }
        for (int i = 0; i < lengthA; i++) {
            for (int j = 0; j < lengthB; j++) {
                if (dist[i][j] > maxLength) {
                    maxLength = dist[i][j];
                }
            }
        }
        return maxLength;
    }
}
