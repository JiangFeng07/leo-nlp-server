package com.leo.nlp.structure;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by lionel on 17/12/19.
 */
public class TernarySearchTreeTest {
    private static TernarySearchTree ternarySearchTree = new TernarySearchTree();

    static {
        long a = System.currentTimeMillis();
        readFile(ternarySearchTree, "/Users/lionel/Desktop/data/recommendDish/dish_statistic.csv");
        long b = System.currentTimeMillis();
        System.out.println(String.format("树构建用时%d", b - a));
    }

    @Test
    public void test() {
//        System.out.println(ternarySearchTree.search("红烧鸡块"));
//        System.out.println(ternarySearchTree.search("鳗鱼盖饭"));
        ternarySearchTree.toFile("/tmp/1.csv");
    }

    private static void readFile(TernarySearchTree ternarySearchTree, String file) {
//        InputStream is = null;
        BufferedReader br = null;
        try {
//            is = TernarySearchTreeTest.class.getResourceAsStream(file);
//            br = new BufferedReader(new InputStreamReader(is));
            br = new BufferedReader(new FileReader(new File(file)));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                ternarySearchTree.insert(line.trim());
                count += 1;
                if (count % 1000 == 0) {
                    System.out.println(String.format("execute %d records!", count));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
    }
}
