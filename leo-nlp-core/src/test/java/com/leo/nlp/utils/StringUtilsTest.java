package com.leo.nlp.utils;

import org.junit.Test;

/**
 * Created by lionel on 17/12/19.
 */
public class StringUtilsTest {
    @Test
    public void word2Pinyin() {
        System.out.println(StringUtil.word2Pinyin("江峰"));
        System.out.println(StringUtil.word2Pinyin("江 峰"));
    }
}
