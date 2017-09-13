package com.leo.nlp.algorithm;

import com.leo.nlp.seg.TextSegment;
import com.leo.nlp.text.TextPreprocessor;
import org.apache.commons.lang3.StringUtils;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import java.util.*;

/**
 * Created by lionel on 17/9/8.
 */
public class PageRank {
    //移动窗口大小
    private static final int WINDOW_SIZE = 2;

    private static final double DAMPING_FACTOR = 0.85;

    // 程序结束阈值
    private static final double ALPHA = 0.0001;

    private static Map<Integer, String> indexMap = new HashMap<Integer, String>();

    public static double[][] buildTransitionMatrix(String text) {
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
            indexMap.put(index, ele);
            index += 1;
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        double[][] matrix = new double[set.size()][set.size()];

        for (int i = WINDOW_SIZE; i < tmpWords.length - WINDOW_SIZE; i++) {
            int x = map.get(tmpWords[i]);
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                if ("*".equals(tmpWords[j])) {
                    continue;
                }
                int y = map.get(tmpWords[j]);
                if (matrix[x][y] == 1.0) {
                    continue;
                }
                matrix[x][y] = 1.0;
                matrix[y][x] = 1.0;
            }

            for (int j = i + 1; j <= i + WINDOW_SIZE; j++) {
                if ("*".equals(tmpWords[j])) {
                    continue;
                }
                int y = map.get(tmpWords[j]);
                if (matrix[x][y] == 1.0) {
                    continue;
                }
                matrix[x][y] = 1.0;
                matrix[y][x] = 1.0;
            }

        }
        if (matrix.length > 0) {
            return matrix;
        }
        return null;
    }

    /**
     * g=ds+(1-d)eeT/n,p_n=g^n*p_0,求 pr 值
     *
     * @param matrix 矩阵
     * @return pr值
     */
    public static Matrix pageRank(double[][] matrix) {
        int row = matrix.length;
        int column = matrix[0].length;

        //初始 pr 值
        Matrix p = DenseMatrix.Factory.zeros(row, 1);
        for (int i = 0; i < row; i++) {
            p.setAsDouble(1.0 / row, i, 0);
        }

        //转移矩阵
        Matrix s = DenseMatrix.Factory.zeros(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                s.setAsDouble(matrix[i][j], i, j);
            }
        }
        List<Matrix> matrixList = s.getRowList();
        double[] sum = new double[matrixList.size()];
        for (int i = 0; i < matrixList.size(); i++) {
            sum[i] = matrixList.get(i).getValueSum();
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (sum[i] == 0) {
                    continue;
                }
                s.setAsDouble(s.getAsDouble(i, j) / sum[i], i, j);
            }
        }
        s = s.transpose();

        Matrix eeT = DenseMatrix.Factory.ones(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                eeT.setAsDouble(eeT.getAsDouble(i, j) / row, i, j);
            }
        }
        Matrix g = s.times(DAMPING_FACTOR).plus(eeT.times((1 - DAMPING_FACTOR)));
        while (true) {
            Matrix newP = g.mtimes(p);
            Matrix error = newP.minus(p);
            for (int i = 0; i < error.getRowCount(); i++) {
                for (int j = 0; j < error.getColumnCount(); j++) {
                    error.setAsDouble(Math.abs(error.getAsDouble(i, j)), i, j);
                }
            }
            double mix = error.getValueSum();

            if (mix <= ALPHA) {
                return newP;
            }
            p = newP;
        }
    }

    public static void main(String[] args) {
        String text = "巴萨是我喜欢的足球俱乐部梅西是巴萨的核心球员";
        double[][] matrix = buildTransitionMatrix(text);
        if (matrix != null) {
            for (double[] ele : matrix) {
                for (double el : ele) {
                    System.out.print(el + " ");
                }
                System.out.println();
            }
            Matrix res = pageRank(matrix);
            for (int i = 0; i < res.getRowList().size(); i++) {
                System.out.println(String.format("【%s】的 page_rank 是 %f ", indexMap.get(i), res.getRowList().get(i).getAsDouble(0, 0)));
            }
        }
    }
}
