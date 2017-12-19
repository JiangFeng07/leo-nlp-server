package com.leo.nlp.structure;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.pinyin.Pinyin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 17/12/18.
 */
public class TernarySearchTree {
    private TSTNode root;

    public void insert(String word) {
        char[] chars = word2Pinyin(word);
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

    public List<String> search(String word) {
        char[] chars = word2Pinyin(word);
        return search(root, chars, 0);
    }

    private List<String> search(TSTNode node, char[] chars, int ptr) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        if (ptr == chars.length) {
            return node.getWords();
        }
        if (node.character == chars[ptr]) {
            if (ptr == chars.length - 1) {
                return node.getWords();
            } else {
                return search(node.middle, chars, ptr + 1);
            }
        } else if (node.character < chars[ptr]) {
            return search(node.right, chars, ptr);
        } else {
            return search(node.left, chars, ptr);
        }
    }

    private char[] word2Pinyin(String word) {
        String pinYin = StringUtils.join(Pinyin.pinyin(word), "");
        if (StringUtils.isBlank(pinYin)) {
            return null;
        }
        return pinYin.toCharArray();
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
