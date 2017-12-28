package com.leo.nlp.algorithm;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lionel on 17/12/26.
 */
public class Bayes {
    private Integer totalDocCount = 0;
    private Map<String, Integer> topicCountMap = new HashMap<>();
    private Map<String, Integer> wordsCountMap = new HashMap<>();
    private Map<String, Set<String>> topicWordsMap = new HashMap<>();
    private Set<String> wordSet = new HashSet<>();

    private void train(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(" ");
                String topic = fields[0];
                String[] words = fields[1].split(",");
                topicCountMap.put(topic, topicCountMap.getOrDefault(topic, 0) + 1);
                totalDocCount += 1;
                for (String word : words) {
                    wordSet.add(word);
                    String key = String.format("%s|%s", word, topic);
                    wordsCountMap.put(key, wordsCountMap.getOrDefault(key, 0) + 1);
                    wordsCountMap.put(topic, wordsCountMap.getOrDefault(topic, 0) + 1);
                    if (!topicWordsMap.containsKey(topic)) {
                        topicWordsMap.put(topic, new HashSet<>());
                    }
                    topicWordsMap.get(topic).add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String modelPath) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(modelPath)));
            writer.write(String.format("wordsize=>%d\n", wordSet.size()));
            for (Map.Entry<String, Integer> entry : topicCountMap.entrySet()) {
                writer.write(String.format("%sProb=>%f\n", entry.getKey(), Math.log(1.0 * entry.getValue() / totalDocCount)));
            }

            for (Map.Entry<String, Integer> entry : wordsCountMap.entrySet()) {
                String key = entry.getKey();
                if (!key.contains("|")) {
                    writer.write(String.format("%s=>%f\n", key, 1.0 * (wordsCountMap.get(key))));
                    continue;
                }
                String[] fields = key.split("\\|");
                writer.write(String.format("%s=>%f\n", key, Math.log(1.0 * (wordsCountMap.get(key) + 1) / (wordsCountMap.get(fields[1]) + wordSet.size()))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public Map<String, Double> lodeModel(String modelPath) {
        Map<String, Double> model = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(modelPath)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split("=>");
                model.put(fields[0], NumberUtils.toDouble(fields[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    public String predict(String text, Map<String, Double> model) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        String[] words = text.split(",");
        double yesProb = model.get("yesProb");
        double noProb = model.get("noProb");
        for (String word : words) {
            String yesKey = String.format("%s|yes", word);
            String noKey = String.format("%s|no", word);
            if (model.containsKey(yesKey)) {
                yesProb += model.get(yesKey);
            } else {
                yesProb += Math.log(1.0 / (model.get("yes") + model.get("wordsize")));
            }
            if (model.containsKey(noKey)) {
                noProb += model.get(noKey);
            } else {
                noProb += Math.log(1.0 / (model.get("no") + model.get("wordsize")));
            }
        }
        System.out.println(String.format("类别 yes 的概率是:%f", yesProb));
        System.out.println(String.format("类别 no 的概率是:%f", noProb));
        if (yesProb > noProb) {
            return "yes";
        }
        return "no";
    }

    public static void main(String[] args) {
        Bayes bayes = new Bayes();
        bayes.train("/tmp/1.csv");
        bayes.topicCountMap.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println();
        bayes.wordsCountMap.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println();
        bayes.topicWordsMap.forEach((k, v) -> System.out.println(k + ":" + v));
        System.out.println();
        bayes.save("/tmp/bayes_model.csv");
        Map<String, Double> model = bayes.lodeModel("/tmp/bayes_model.csv");
        System.out.println(bayes.predict("Chinese,Chinese,Chinese,Tokyo,Japan", model));
    }
}
