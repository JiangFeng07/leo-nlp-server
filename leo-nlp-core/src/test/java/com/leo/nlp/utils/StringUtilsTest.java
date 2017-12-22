package com.leo.nlp.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

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


    //    public static void main(String[] args) {
//        List<String> sentences = loadFile("/Users/lionel/Downloads/Review.csv");
////        sentences.stream().forEach(System.out::println);
////        count("/Users/lionel/Downloads/review1.csv");
//        BufferedWriter writer;
//        try {
//            writer = new BufferedWriter(new FileWriter(new File("/tmp/train.csv")));
//            sentences.stream().forEach(e -> {
//                try {
//                    writer.write(e + "\n");
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
//    }
}
