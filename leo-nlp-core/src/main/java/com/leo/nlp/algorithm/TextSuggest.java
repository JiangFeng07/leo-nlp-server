package com.leo.nlp.algorithm;

import com.leo.nlp.structure.TernarySearchTree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lionel on 17/8/21.
 */
@Slf4j
public class TextSuggest {
    private static TernarySearchTree ternarySearchTree = new TernarySearchTree();

    static {
        buildTree("/minreng.dic");
    }

    private static void buildTree(String path) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = TextSuggest.class.getResourceAsStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                ternarySearchTree.insert(line.trim());
            }
        } catch (IOException e) {
            log.info("IO error", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
    }
}
