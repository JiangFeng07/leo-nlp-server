package com.leo.nlp.algorithm.stringdistance;

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
        int[][] road = new int[lengthA][lengthB];

        for (int i = lengthA - 1; i >= 0; i--) {
            for (int j = lengthB - 1; j >= 0; j--) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    dist[i][j] = dist[i + 1][j + 1] + 1;
                    road[i][j] = 1;
                } else {
//                    dist[i][j] = Math.max(dist[i + 1][j], dist[i][j + 1]);
                    if (dist[i + 1][j] > dist[i][j + 1]) {
                        dist[i][j] = dist[i + 1][j];
                        road[i][j] = 2;
                    } else {
                        dist[i][j] = dist[i][j + 1];
                        road[i][j] = 3;
                    }
                }
            }
        }
        for (int[] ele1 : dist) {
            for (int ele : ele1) {
                System.out.print(ele + " ");
            }
            System.out.println();
        }
        System.out.println("===========================");
        for (int[] ele1 : road) {
            for (int ele : ele1) {
                System.out.print(ele + " ");
            }
            System.out.println();
        }
        return dist[0][0];
    }

    public static void main(String[] args) {
        getLongestCommonSubSequence("delete", "leet");
    }
}
