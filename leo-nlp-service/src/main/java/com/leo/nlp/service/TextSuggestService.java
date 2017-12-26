package com.leo.nlp.service;

import com.leo.nlp.algorithm.NGrams;
import com.leo.nlp.structure.BKTree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lionel on 17/8/22.
 */
@Service
public class TextSuggestService {
    public String correctSuggest(String text) {
        Map<String, Double> finalMap = new HashMap<>();
        Map<String, Double> map = suggest(text);
        map.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        for (String key : finalMap.keySet()) {
            return key;
        }
        return null;
    }

    public static Map<String, Double> suggest(String text) {
        if (StringUtils.isBlank(text)) {
            return new HashMap<>();
        }
        List<String> candidates = BKTree.search(text, 1);
        if (candidates == null) {
            return new HashMap<>();
        }
        Map<String, Double> map = new HashMap<>();
        for (String candidate : candidates) {
            if (map.containsKey(candidate)) {
                continue;
            }
            map.put(candidate, NGrams.getPerplexity(candidate));
        }
        return map;
    }
}
