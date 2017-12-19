package com.leo.nlp.structure;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lionel on 17/12/19.
 */
public class TernarySearchTreeTest {
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

    private static void readFile(TernarySearchTree ternarySearchTree, String file) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = TernarySearchTreeTest.class.getResourceAsStream(file);
            br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                ternarySearchTree.insert(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
    }
}
