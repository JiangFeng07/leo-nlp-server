package com.leo.nlp.structure;

import com.leo.nlp.utils.StringUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 17/12/18.
 */
public class TernarySearchTree {
    private TSTNode root;

    public void insert(String word) {
        char[] chars = StringUtil.word2Pinyin(word).toCharArray();
        root = insert(root, word, chars, 0);
    }

    private TSTNode insert(TSTNode node, String word, char[] chars, int ptr) {
        if (chars == null || chars.length <= 0) {
            return node;
        }
        if (node == null) {
            node = new TSTNode(chars[ptr]);
        }
        if (chars[ptr] < node.character) {
            node.left = insert(node.left, word, chars, ptr);
        } else if (chars[ptr] > node.character) {
            node.right = insert(node.right, word, chars, ptr);
        } else {
            if (ptr + 1 < chars.length) {
                node.middle = insert(node.middle, word, chars, ptr + 1);
            } else {
                if (node.words == null) {
                    node.words = new ArrayList<String>();
                    node.words.add(word);
                }
                if (!node.words.contains(word)) {
                    node.words.add(word);
                }
            }
        }
        return node;
    }

    public boolean search(String word) {
        if (StringUtils.isBlank(word)) {
            return false;
        }
        char[] chars = StringUtil.word2Pinyin(word).toCharArray();
        return search(root, word, chars, 0);
    }

    private boolean search(TSTNode node, String word, char[] chars, int ptr) {
        if (node.character == chars[ptr]) {
            if (ptr == chars.length - 1) {
                return node.getWords() != null && node.getWords().contains(word);
            } else {
                return search(node.middle, word, chars, ptr + 1);
            }
        } else if (node.character < chars[ptr]) {
            return search(node.right, word, chars, ptr);
        } else {
            return search(node.left, word, chars, ptr);
        }
    }

    public List<String> suggest(String word) {
        char[] chars = StringUtil.word2Pinyin(word).toCharArray();
        return suggest(root, chars, 0);
    }

    private List<String> suggest(TSTNode node, char[] chars, int ptr) {
        if (node.character == chars[ptr]) {
            if (ptr == chars.length - 1) {
                if (node.getWords() == null) {
                    return findNearestWords(node);
                } else {
                    return node.getWords();
                }
            } else {
                return suggest(node.middle, chars, ptr + 1);
            }
        } else if (node.character > chars[ptr]) {
            return suggest(node.left, chars, ptr);
        } else {
            return suggest(node.right, chars, ptr);
        }
    }

    private List<String> findNearestWords(TSTNode node) {
        if (node == null) {
            return new ArrayList<String>();
        }
        if (node.words != null) {
            return node.getWords();
        }
        List<String> result = findNearestWords(node.left);
        result.addAll(findNearestWords(node.right));
        result.addAll(findNearestWords(node.middle));
        return result;
    }


    @Data
    private class TSTNode {
        char character;//英文字符
        TSTNode left;//左节点
        TSTNode middle;//中间节点
        TSTNode right;//右节点
        List<String> words = null; //词语集合

        public TSTNode(char character) {
            this.character = character;
        }
    }
}
