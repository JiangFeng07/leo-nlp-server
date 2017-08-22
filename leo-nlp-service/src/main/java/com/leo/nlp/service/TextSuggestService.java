package com.leo.nlp.service;

import com.leo.nlp.algorithm.TextSuggest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by lionel on 17/8/22.
 */
@Service
public class TextSuggestService {
    public String suggest(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        return TextSuggest.suggest(text);
    }

    public static void main(String[] args) {
        TextSuggestService textSuggestService = new TextSuggestService();
        System.out.println(textSuggestService.suggest("毛泽东"));
    }
}
