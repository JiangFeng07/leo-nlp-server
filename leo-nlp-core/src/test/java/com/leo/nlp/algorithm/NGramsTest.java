package com.leo.nlp.algorithm;

import org.junit.Test;

/**
 * Created by lionel on 17/12/22.
 */
public class NGramsTest {
    @Test
    public void test() {
        System.out.println(NGrams.getPerplexity("红烧肉"));
    }
}
