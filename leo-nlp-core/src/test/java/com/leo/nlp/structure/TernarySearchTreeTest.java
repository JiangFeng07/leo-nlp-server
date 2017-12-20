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
    private static TernarySearchTree ternarySearchTree = new TernarySearchTree();

    static {
        readFile(ternarySearchTree, "/dish.csv");
    }

    @Test
    public void test() {
        System.out.println(ternarySearchTree.suggest("红烧"));
        System.out.println(ternarySearchTree.search("红烧鸡块"));
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
