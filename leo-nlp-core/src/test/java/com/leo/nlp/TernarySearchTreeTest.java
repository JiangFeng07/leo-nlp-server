package com.leo.nlp;

import com.leo.nlp.structure.TernarySearchTree;
import org.junit.Test;

/**
 * Created by lionel on 17/8/18.
 */
public class TernarySearchTreeTest {
    @Test
    public void test() {
        TernarySearchTree ternarySearchTree = new TernarySearchTree();
        ternarySearchTree.insert("毛泽东");
        ternarySearchTree.insert("江泽民");
        ternarySearchTree.insert("邓小平");
        System.out.println(ternarySearchTree.search("毛泽东"));
        System.out.println(ternarySearchTree.toString());
        ternarySearchTree.delete("毛泽东");
        System.out.println(ternarySearchTree.toString());
    }
}
