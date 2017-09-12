package com.leo.nlp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lionel on 17/8/18.
 */
public class StringUtil {
    public static long[] string2Vector(String[] words, List<String> list) {
        if (words == null || words.length <= 0 || list == null || list.size() <= 0) {
            return null;
        }
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

    public static void main(String[] args) {
        String[] words = new String[]{"江峰", "梅西", "江峰", "足球"};
        List<String> list = new ArrayList<String>();
        list.add("江峰");
        list.add("梅西");
        list.add("罗纳尔多");
        list.add("巴萨");
        list.add("足球");
        if (string2Vector(words, list) != null) {
            for (long ele : string2Vector(words, list)) {
                System.out.print(ele + " ");
            }
        }
    }
}
