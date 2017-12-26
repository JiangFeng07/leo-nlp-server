package com.leo.nlp.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lionel on 17/12/21.
 */

@Slf4j
public class NGrams {
    private static int maxN;
    private static ArrayList<Integer> gramsNumb = new ArrayList<>();
    private static HashMap<String, Double[]> NGramsModel = new HashMap<>();


    static {
        loadModel("/Users/lionel/Desktop/srilm/LM");
    }

    // load srilm model from file
    private static void loadModel(String path) {
//        InputStream is = NGrams.class.getResourceAsStream(relativePath);
        try {
//            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            BufferedReader bf = new BufferedReader(new FileReader(new File(path)));
            String line;
            // 控制当前读取的ngram类型
            int readType = 0;
            while ((line = bf.readLine()) != null) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }

                if (readType == 0 && line.contains("=")) {
                    // read ngram 1=14501 like data
                    String[] pieces = line.split("=");
                    maxN++;
                    gramsNumb.add(NumberUtils.toInt(pieces[1]));
                }

                if (line.contains("-grams:")) {
                    readType = NumberUtils.toInt(line.split("-")[0].replace("\\", ""));
                    continue;
                }

                if (readType == 0) {
                    continue;
                }

                String[] columns = line.split("\t");
                // with backoff value
                if (columns.length == 3) {
                    NGramsModel.put(columns[1], new Double[]{NumberUtils.toDouble(columns[0]), NumberUtils.toDouble(columns[2])});
                } else if (columns.length == 2) {
                    NGramsModel.put(columns[1], new Double[]{NumberUtils.toDouble(columns[0]), 0.0});
                }
            }
        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    // get log prob for splited sentence
    public static List<Double> predictLogProb(String text, int N) {
        if (N > maxN) {
            log.error("Maximum N value is ", maxN);
            return null;
        }
        List<String> spitedText = Arrays.asList(("<s> " + text + " </s>").split(" "));
        List<Double> resultList = new ArrayList<>();

        for (int i = 1; i < spitedText.size(); i++) {
            int j = i - 1;
            // 当前词的后验概率
            double currProb = 0.0;

            // 如果当前词是新词则直接返回当前概率0.0否则将currProb设定为1Gram下该词概率
            if (NGramsModel.containsKey(spitedText.get(i))) {
                currProb = NGramsModel.get(spitedText.get(i))[0];
            } else {
                resultList.add(currProb);
                continue;
            }

            // 从2Gram开始向前回溯直到句首或NGram
            while (j >= 0 && i - j <= N - 1) {
                Double[] tempWithoutWord = NGramsModel.get(StringUtils.join(spitedText.subList(j, i), " "));
                // 当前回溯前缀不在模型中 没有必要继续回溯 中止
                if (tempWithoutWord == null) {
                    break;
                }

                // 获取当前wj...wi-1 -> wi模式下的概率
                Double[] temp = NGramsModel.get(StringUtils.join(spitedText.subList(j, i + 1), " "));

                // 模式不存在 添加wj...wi-1模式的backoff值到概率上（根据Katz Smooth应该为backoff*prob，但是因为这里srilm模型结果做了对数转化 所以直接相加即可）
                if (temp == null) {
                    currProb += tempWithoutWord[1];
                }
                // 模式存在 则更新currProb为当前模式概率
                else {
                    currProb = temp[0];
                }
                j--;
            }
            resultList.add(currProb);
        }
        return resultList;
    }

    // get perplexity value from logProb list
    private static double getPerplexity(List<Double> logProbList) {
        double result = 0.0;
        int nonZero = 0;
        for (double d : logProbList) {
            if (!BigDecimal.valueOf(d).equals(BigDecimal.valueOf(0.0))) {
                nonZero++;
                result += d;
            }
        }
        result = Math.pow(Math.pow(10, result), -1.0 / nonZero);
        return result;
    }

    public static double getPerplexity(String text) {
        String word = StringUtils.join(Arrays.asList(text.split("")), " ");
        return getPerplexity(predictLogProb(word, 3));
    }
}
