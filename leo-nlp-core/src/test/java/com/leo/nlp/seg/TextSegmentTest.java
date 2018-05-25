package com.leo.nlp.seg;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lionel on 17/12/20.
 */
public class TextSegmentTest {
    @Test
    public void segment() {
        String text = "这里的辣菜，味道非常的不错，喜欢吃辣的来这里，真的是很不错的哦。辣子对虾，辣的恰到好处，虾肉非常的嫩。。酸菜鱼，鱼肉很嫩滑，汤又是酸酸的，吃起来很开胃呢。红枣杏仁豆腐，豆腐非常的入味。还有口水鸡，够辣，吃的是一身汗，冬天去吃的话，非常适合哦。吃的全身热乎乎的，感觉一定非常的好呢。 服务也绝对";
        System.out.println(TextSegment.parseForDishName(text));
    }

    @Test
    public void test() {
        String text = "故乡是温暖的,它让人舒适;故乡是温馨的,它让人憧憬;故乡是快乐的,它让人怀念";
        System.out.println(ToAnalysis.parse(text));
    }

    @Test
    public void test2() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(new File("/Users/lionel/Downloads/dr.csv")));
            writer = new BufferedWriter(new FileWriter(new File("/tmp/1.csv")));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split("\\t");
                if (fields.length != 2) {
                    continue;
                }
                Result result = TextSegment.parseForDishName(fields[1]);
                List<String> words = result.getTerms().stream()
                        .filter(e -> !e.getNatureStr().equals("null"))
                        .filter(e -> !e.getNatureStr().startsWith("w"))
                        .filter(e -> !e.getNatureStr().startsWith("t"))
                        .map(Term::getName).collect(Collectors.toList());
                writer.write(String.format("%s %s\n", fields[0], StringUtils.join(words, ",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
    }

    @Test
    public void test3() {
        String text = "黄金甲颜色富贵的华丽，粉碎成齿间分离的清脆，流淌出甜蜜香浓的妩媚，缠绕心情坠入无忧无虑的谷底，为此沉迷不醒万劫不复亦在所不惜......";
        System.out.println(ToAnalysis.parse(text));
    }
}
