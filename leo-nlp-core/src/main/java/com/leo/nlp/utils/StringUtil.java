package com.leo.nlp.utils;

import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.pinyin.Pinyin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lionel on 17/8/18.
 */
public class StringUtil {
    public static final Pattern PATTERN_ENGLISH = Pattern.compile("[a-zA-Z]+");

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
        List<String> rs = new ArrayList<String>();
        Matcher m = p.matcher(str);
        while (m.find()) {
            rs.add(m.group());
        }
        return rs;
    }
}
