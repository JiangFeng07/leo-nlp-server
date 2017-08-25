package com.leo.nlp.algorithm;

/**
 * Created by lionel on 17/8/25.
 */

import com.leo.nlp.structure.DGraph;
import com.leo.nlp.structure.ListDGraph;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 文本提取关键词
 */
public class ExtractKeyWord {
    private static final int WINDOW_SIZE = 4;
    private static ListDGraph<String> dGraph = new ListDGraph<String>();

    /**
     * 利用分完词后的文本建立有向图
     *
     * @param text 分完词后的文本,文本中每个词以空格分割
     */
    private static void buildDGraph(String text) {
        if (StringUtils.isBlank(text)) {
            return;
        }
        text = String.format("* * * * * %s * * * * *", text);
        String[] words = text.trim().split(" ");
        System.out.println(words.length);
        Set<String> set = new HashSet<String>(Arrays.asList(words));
        for (String next : set) {
            if (!next.equals("*")) {
                dGraph.add(next);
            }
        }
        for (int i = WINDOW_SIZE; i < words.length - WINDOW_SIZE; i++) {
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                if ("*".equals(words[j]) || words[i].equals(words[j])) {
                    continue;
                }
                dGraph.add(new DGraph.Edge<String>(words[i], words[j]));
            }
            for (int m = i + 1; m <= i + WINDOW_SIZE; m++) {
                if ("*".equals(words[m]) || words[i].equals(words[m])) {
                    continue;
                }
                dGraph.add(new DGraph.Edge<String>(words[i], words[m]));
            }
        }
    }

    public static void main(String[] args) {
        String text = "程序员 英文 程序 开发 维护 专业 人员 程序员 分为 程序 设计 人员 程序 编码 人员 界限 特别 中国 软件 人员 分为 程序员 高级 程序员 系统 分析员 项目 经理";
        ExtractKeyWord.buildDGraph(text);
        Iterator<String> iterator = dGraph.iterator(DGraph.ITERATOR_TYPE_BFS, "程序员");
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.print(str + "->");
        }
    }
}
