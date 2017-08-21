package com.leo.nlp.algorithm;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by lionel on 17/8/18.
 */
public class EditDistance implements StringDistance {
    public double getDistance(String s1, String s2) {
        return 1.0 * getEditDistance(s1, s2) / Math.min(s1.length(), s2.length());
    }

    private int getEditDistance(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        }

        int lengthA = s1.length();
        int lengthB = s2.length();

        if (lengthA == 0 || lengthB == 0) {
            return Math.max(lengthA, lengthB);
        }

        int[][] dist = new int[lengthA + 1][lengthB + 1];
        for (int i = 0; i <= lengthA; i++) {
            dist[i][0] = i;
        }
        for (int j = 0; j <= lengthB; j++) {
            dist[0][j] = j;
        }

        for (int i = 1; i <= lengthA; i++) {
            for (int j = 1; j <= lengthB; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dist[i][j] = Collections.min(Arrays.asList(dist[i - 1][j] + 1, dist[i][j - 1] + 1, dist[i - 1][j - 1] + cost));
            }
        }

        return dist[lengthA][lengthB];
    }
}
