package com.leo.nlp.structure;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 17/8/18.
 */
public class TernarySearchTree {
    private TSTNode root;
    private ArrayList<String> al;

    /**
     * 构造函数
     **/
    public TernarySearchTree() {
        root = null;
    }

    /**
     * 判断 TernarySearchTree 是否为空
     **/
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * 清空 TernarySearchTree
     **/
    public void makeEmpty() {
        root = null;
    }

    /**
     * 插入字符串
     **/
    public void insert(String word) {
        root = insert(root, word, 0);
    }

    private TSTNode insert(TSTNode r, String word, int ptr) {
        if (StringUtils.isBlank(word)) {
            return r;
        }
        char[] words = word.toCharArray();
        if (r == null) {
            r = new TSTNode(words[ptr]);
        }
        if (words[ptr] < r.data) {
            r.left = insert(r.left, word, ptr);
        } else if (words[ptr] > r.data) {
            r.right = insert(r.right, word, ptr);
        } else {
            if (ptr + 1 < words.length) {
                r.middle = insert(r.middle, word, ptr + 1);
            } else {
                r.isEnd = true;
                if (!r.words.contains(word)) {
                    r.words.add(word);
                }
            }
        }
        return r;
    }

    /**
     * 删除字符串
     **/
    public void delete(String word) {
        delete(root, word.toCharArray(), 0);
    }

    private void delete(TSTNode r, char[] word, int ptr) {
        if (r == null)
            return;

        if (word[ptr] < r.data) {
            delete(r.left, word, ptr);
        } else if (word[ptr] > r.data) {
            delete(r.right, word, ptr);
        } else {
            /** to delete a word just make isEnd false **/
            if (r.isEnd && ptr == word.length - 1) {
                r.isEnd = false;
            } else if (ptr + 1 < word.length) {
                delete(r.middle, word, ptr + 1);
            }
        }
    }

    /**
     * 查找字符串
     **/
    public boolean search(String word) {
        return search(root, word.toCharArray(), 0);
    }

    private boolean search(TSTNode r, char[] word, int ptr) {
        if (r == null) {
            return false;
        }

        if (word[ptr] < r.data) {
            return search(r.left, word, ptr);
        } else if (word[ptr] > r.data) {
            return search(r.right, word, ptr);
        } else {
            if (r.isEnd && ptr == word.length - 1) {
                return true;
            } else if (ptr == word.length - 1) {
                return false;
            } else {
                return search(r.middle, word, ptr + 1);
            }
        }
    }

    /**
     * 打印 TernarySearchTree
     **/
    public String toString() {
        al = new ArrayList<String>();
        traverse(root, "");
        return "\nTernary Search Tree : " + al;
    }

    /**
     * 遍历 TernarySearchTree
     **/
    private void traverse(TSTNode r, String str) {
        if (r == null) {
            return;
        }

        traverse(r.left, str);
        str = str + r.data;
        if (r.isEnd) {
            al.add(str);
        }
        traverse(r.middle, str);
        str = str.substring(0, str.length() - 1);
        traverse(r.right, str);
    }


    /**
     * 文本相似推荐
     */
    public List<String> suggest(String word) {
        List<String> suggestWords = new ArrayList<String>();
        if (StringUtils.isBlank(word)) {
            return suggestWords;
        }
        suggestWords = suggest(root, word.toCharArray(), 0);
        return suggestWords;
    }

    private List<String> suggest(TSTNode r, char[] word, int ptr) {
        if (r == null) {
            return null;
        }
        List<String> res = new ArrayList<String>();
        if (word[ptr] < r.data) {
            return suggest(r.left, word, ptr);
        } else if (word[ptr] > r.data) {
            return suggest(r.right, word, ptr);
        } else {
            if (r.isEnd && ptr == word.length - 1) {
                res.addAll(r.words);
                return res;
            } else if (!r.isEnd && ptr == word.length - 1) {
                return null;
            } else {
                return suggest(r.middle, word, ptr + 1);
            }
        }
    }


    @Data
    private class TSTNode {
        char data;
        boolean isEnd;
        TSTNode left;
        TSTNode middle;
        TSTNode right;
        List<String> words;

        /**
         * Constructor
         **/
        public TSTNode(char data) {
            this.data = data;
            this.isEnd = false;
            this.left = null;
            this.middle = null;
            this.right = null;
            this.words = new ArrayList<String>();
        }
    }
}
