package com.leo.nlp.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.pinyin.Pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by lionel on 17/8/18.
 */
public class StringUtil {
    public static final Pattern PATTERN_ENGLISH = Pattern.compile("[a-zA-Z]+");
    public static final Pattern PATTERN_JAPANESE = Pattern.compile("[\\u3040-\\u32FF]+");
    public static final Pattern PATTERN_CHINESE = Pattern.compile("[\\u4e00-\\u9fa5]+");
    public static final Pattern PATTERN_KOREAN = Pattern.compile("[\\uAC00-\\uD7AF]+");


    /**
     * 文本转化为向量
     *
     * @param words 文本分词后的结果
     * @param list  文本列表
     * @return 文本转化为向量
     */
    public static long[] string2Vector(String[] words, List<String> list) {
        if (words == null || words.length <= 0 || list == null || list.size() <= 0) {
            return null;
        }

        //计算单词频次
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String word : words) {
            if (!map.containsKey(word)) {
                map.put(word, 0);
            }
            map.put(word, map.get(word) + 1);
        }
        long[] array = new long[list.size()];
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (list.contains(key)) {
                int index = list.indexOf(key);
                array[index] = value;
            }
        }
        return array;
    }

    public static String word2Pinyin(String word) {
        if (StringUtils.isBlank(word)) {
            return "";
        }
        if (extract(PATTERN_ENGLISH, word).isEmpty()) {
            return StringUtils.join(Pinyin.pinyin(word), "");
        }

        StringBuilder sb = new StringBuilder();
        for (char ch : word.toLowerCase().toCharArray()) {
            //是否是汉字
            if (ch >= 0x4E00 && ch <= 0x9FA5) {
                String pinyin = org.apache.commons.lang3.StringUtils.join(Pinyin.pinyin(String.valueOf(ch)), "");
                sb.append(pinyin);
            }
            //是否为英文字母
            if ((ch >= 0x0041 && ch <= 0x005A) || (ch >= 0x0061 && ch <= 0x007A)) {
                sb.append(String.valueOf(ch));
            }
        }
        return sb.toString();
    }

    public static List<String> extract(Pattern p, String str) {
        List<String> rs = new ArrayList<>();
        Matcher m = p.matcher(str);
        while (m.find()) {
            rs.add(m.group());
        }
        return rs;
    }

    public static boolean isChinese(String word) {
        if (StringUtils.isBlank(word)) {
            return false;
        }
        for (char ch : word.toCharArray()) {
            if (ch >= 0x4E00 && ch <= 0x9FA5) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * n_gram 数据预处理
     *
     * @param path
     * @return
     */
    public static List<String> loadFile(String path) {
        List<String> sentences = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(path)));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] files = line.trim().split("[。.?!？！]");
                if (files.length <= 0) {
                    continue;
                }
                sentences.addAll(Arrays.asList(files).stream()
                        .map(e -> StringUtil.extract(StringUtil.PATTERN_CHINESE, e.trim()))
                        .map(e -> StringUtils.join(e, ""))
                        .filter(e -> e.length() >= 4)
                        .map(e -> StringUtils.join(Arrays.asList(e.split("")), " "))
                        .collect(Collectors.toSet()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return sentences;
    }
}
