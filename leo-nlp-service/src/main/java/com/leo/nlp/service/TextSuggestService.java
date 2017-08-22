package com.leo.nlp.service;


import com.leo.nlp.algorithm.TextSuggest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lionel on 17/8/22.
 */
@Service
public class TextSuggestService {
    public List<String> suggest(String text) {
        return TextSuggest.suggest(text);
    }
}
