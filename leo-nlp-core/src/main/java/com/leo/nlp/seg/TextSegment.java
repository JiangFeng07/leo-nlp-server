package com.leo.nlp.seg;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lionel on 17/9/12.
 */
public class TextSegment {
    public static String[] parse(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        Result result = ToAnalysis.parse(text);
        int length = result.getTerms().size();
        String[] words = new String[length];
        for (int i = 0; i < length; i++) {
            String name = result.getTerms().get(i).getName();
            words[i] = name;
        }
        return words;
    }

    public static void main(String[] args) {
        String[] words = TextSegment.parse("巴萨是我最喜欢的足球俱乐部");
        if (words != null && words.length > 0) {
            for (String word : words)
                System.out.print(word + " ");
        }
    }
}
