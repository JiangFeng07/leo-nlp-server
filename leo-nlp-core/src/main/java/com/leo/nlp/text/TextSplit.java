package com.leo.nlp.text;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lionel on 17/9/13.
 */
public class TextSplit {
    private static final String SENTENCE_DELIMITERS = "[?!;？！,，.。；\\r\\n]";

    public static String[] sentenceSplit(String sentence) {
        if (StringUtils.isBlank(sentence)) {
            return null;
        }

        String[] res = sentence.split(SENTENCE_DELIMITERS);
        if (res.length > 0) {
            return res;
        }
        return null;
    }
}
