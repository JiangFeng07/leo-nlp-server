package com.leo.nlp.structure;

import org.junit.Test;

/**
 * Created by lionel on 17/12/19.
 */
public class DishNameTSTTest {
    @Test
    public void test() {
        TernarySearchTree ternarySearchTree = new TernarySearchTree();
        ternarySearchTree.insert("数据");
        ternarySearchTree.insert("数学");
        ternarySearchTree.insert("江峰");
        ternarySearchTree.insert("例子");
        ternarySearchTree.insert("栗子");
        ternarySearchTree.insert("梨子");
        System.out.println(ternarySearchTree.search("李子"));
    }
}
