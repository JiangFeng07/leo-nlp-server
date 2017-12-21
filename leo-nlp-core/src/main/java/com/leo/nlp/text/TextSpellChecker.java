package com.leo.nlp.text;

import com.leo.nlp.seg.TextSegment;
import com.leo.nlp.structure.TernarySearchTree;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lionel on 17/12/20.
 */
public class TextSpellChecker {
    private static Map<String, Integer> map = new HashMap<>();
    private static Map<String, String> homonym = new HashMap<>();
    private static Map<String, Double> wordScore = new HashMap<>();
    private static TernarySearchTree ternarySearchTree = new TernarySearchTree();
    private static int WORDS_COUNT = 0;

    static {
        loadReviewFile("/Users/lionel/Downloads/review.csv");
        loadHomonymFile("/Users/lionel/workspace_me/leo-nlp-server/leo-nlp-core/src/main/resources/correct_vocabulary.csv");
        probability();
    }

    private static void loadReviewFile(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                Result result = TextSegment.parseForDishName(line);
                List<Term> terms = result.getTerms();
                for (Term term : terms) {
                    if (!term.getNatureStr().equals("dish")) {
                        continue;
                    }
                    String name = term.getName();
                    map.put(name, map.getOrDefault(name, 0) + 1);
                    WORDS_COUNT += 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static void loadHomonymFile(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.toLowerCase().trim().split(":");
                if (fields.length != 2) {
                    continue;
                }
                String key = fields[1];
                String value = fields[0];
                homonym.put(key, homonym.getOrDefault(fields[1], "") + value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }

    }

    private static void probability() {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            double prob = 1.0 * entry.getValue() / WORDS_COUNT;
            System.out.println(entry.getKey() + ":" + prob);
            wordScore.put(entry.getKey(), prob);
        }
    }

    private static List<String> candidates(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        List<String> candidates = new ArrayList<>();
        candidates.add(text);
        String[] characters = text.split("");
        for (String character : characters) {
            String value = homonym.get(character);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            for (String ele : value.split("")) {
                String tmp = text.replace(character, ele);
                candidates.add(tmp);
            }
        }
        return candidates;
    }

    public static String correct(String text) {
        List<String> candidates = candidates(text);
        if (candidates == null || candidates.size() <= 0) {
            return null;
        }
        double maxScore = 0;
        String result = "";
        for (String candidate : candidates) {
            if (!wordScore.containsKey(candidate)) {
                continue;
            }
            if (wordScore.get(candidate) > maxScore) {
                maxScore = wordScore.get(candidate);
                result = candidate;
            }
        }
        return result;
    }
}
