package com.leo.nlp.algorithm.stringdistance;

import com.leo.nlp.algorithm.CosSimilarity;
import com.leo.nlp.seg.TextSegment;
import com.leo.nlp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 17/9/12.
 */
public class CosDistance implements StringDistance {
    @Override
    public double getDistance(String s1, String s2) {
        if (org.apache.commons.lang3.StringUtils.isBlank(s1) || org.apache.commons.lang3.StringUtils.isBlank(s2)) {
            return 0.0;
        }
        String[] arrayA = TextSegment.parse(s1);
        String[] arrayB = TextSegment.parse(s2);
        if (arrayA == null || arrayA.length <= 0 || arrayB == null || arrayB.length <= 0) {
            return 0.0;
        }
        List<String> list = new ArrayList<String>();
        add(list, arrayA);
        add(list, arrayB);
        long[] a = StringUtil.string2Vector(arrayA, list);
        long[] b = StringUtil.string2Vector(arrayB, list);
        return CosSimilarity.cos(a, b);
    }

    private static void add(List<String> list, String[] array) {
        for (String ele : array) {
            if (list.contains(ele)) {
                continue;
            }
            list.add(ele);
        }
    }

    public static void main(String[] args) {
        String str = "曼联是我最喜欢的足球俱乐部";
        String str2 = "巴萨是我最喜欢的俱乐部";
        CosDistance cosDistance = new CosDistance();
        System.out.println(cosDistance.getDistance(str, str2));
    }
}
