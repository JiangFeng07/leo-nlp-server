package com.leo.nlp.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lionel on 17/12/19.
 */
public class StringUtilsTest {
    @Test
    public void word2Pinyin() {
        System.out.println(StringUtil.word2Pinyin("江峰"));
        System.out.println(StringUtil.word2Pinyin("江 峰"));
    }

    @Test
    public void extract() {
        List<String> result = StringUtil.extract(StringUtil.PATTERN_CHINESE, "味道还不错的，也不是很辣的那种，大众口味的那种");
        System.out.println(StringUtils.join(result, ""));
    }

    @Test
    public void isChinese() {
        System.out.println(StringUtil.isChinese("梅西"));
        System.out.println(StringUtil.isChinese("梅西,"));
        System.out.println(StringUtil.isChinese("梅 西,"));
        System.out.println(StringUtil.isChinese("messi"));
    }

    /**
     * 文本预处理
     */
    @Test
    public void test() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/lionel/Downloads/Review.csv")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/Users/lionel/Desktop/Review.csv")));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.length() < 5) {
                    continue;
                }
                List<String> result = StringUtil.extract(StringUtil.PATTERN_CHINESE, line.trim());
                String sentence = StringUtils.join(result, "");
                writer.write(StringUtils.join(Arrays.asList(sentence.split("")), " ") + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(new File("/Users/lionel/Desktop/data/recommendDish/dish_name_pre.csv")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/Users/lionel/Desktop/data/recommendDish/dish_name_pre2.csv")));

            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtil.isChinese(line.trim())) {
                    writer.write(line.trim() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
