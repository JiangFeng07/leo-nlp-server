package com.leo.nlp.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by lionel on 17/12/20.
 */
public class Frequency {
    public <T> Token<T> getSortList(Map<T, Integer> freq) {
        if (freq == null || freq.size() == 0) {
            return null;
        }
        List<Token<T>> tokens = new ArrayList<Token<T>>(freq.size());
        for (T entry : freq.keySet()) {
            tokens.add(new Token<T>(entry, freq.get(entry)));
        }
        Collections.sort(tokens);
        return tokens.get(0);
    }

    @Data
    public class Token<T> implements Comparable<Token> {
        private T entry;
        private Integer count;

        public Token(T entry, Integer count) {
            this.entry = entry;
            this.count = count;
        }

        public int compareTo(Token token) {
            if (token == null) {
                return 0;
            }
            return token.count.compareTo(count);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            return super.equals(object);
        }
    }
}
