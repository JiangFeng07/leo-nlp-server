package com.leo.nlp.algorithm;

import com.leo.nlp.seg.TextSegment;
import com.leo.nlp.text.TextPreprocessor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by lionel on 17/9/8.
 */
public class PageRank {
    //移动窗口大小
    private static final int WINDOW_SIZE = 2;

    private static final double DAMPING_FACTOR = 0.85;

    private static final double ALPHA = 0.0001;

    public static int[][] buildTransitionMatrix(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        String[] words = TextSegment.parse(text);
        words = TextPreprocessor.removeStopWord(words);
        int length = words.length + 2 * WINDOW_SIZE;
        String[] tmpWords = new String[length];
        for (int i = 0; i < WINDOW_SIZE; i++) {
            tmpWords[i] = "*";
        }
        for (int i = length - 1; i >= length - WINDOW_SIZE; i--) {
            tmpWords[i] = "*";
        }
        System.arraycopy(words, 0, tmpWords, WINDOW_SIZE, length - 2 * WINDOW_SIZE);

        Set<String> set = new HashSet<String>();
        set.addAll(Arrays.asList(words));
        if (set.size() <= 0) {
            return null;
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        int index = 0;
        for (String ele : set) {
            map.put(ele, index);
            index += 1;
        }

        int[][] matrix = new int[set.size()][set.size()];

        for (int i = WINDOW_SIZE; i < tmpWords.length - WINDOW_SIZE; i++) {
            int x = map.get(tmpWords[i]);
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                if ("*".equals(tmpWords[j])) {
                    continue;
                }
                int y = map.get(tmpWords[j]);
                if (matrix[x][y] == 1) {
                    continue;
                }
                matrix[x][y] = 1;
                matrix[y][x] = 1;
            }

            for (int j = i + 1; j <= i + WINDOW_SIZE; j++) {
                if ("*".equals(tmpWords[j])) {
                    continue;
                }
                int y = map.get(tmpWords[j]);
                if (matrix[x][y] == 1) {
                    continue;
                }
                matrix[x][y] = 1;
                matrix[y][x] = 1;
            }

        }
        if (matrix.length > 0) {
            return matrix;
        }
        return null;
    }

    public static double[] pageRank(double[][] matrix) {
        return null;
    }

    public static void main(String[] args) {
        String text = "巴萨是我喜欢的足球俱乐部梅西是巴萨的核心球员";
        int[][] matrix = buildTransitionMatrix(text);
        if (matrix != null) {
            for (int[] ele : matrix) {
                for (int el : ele) {
                    System.out.print(el + " ");
                }
                System.out.println();
            }
        }
    }
}
