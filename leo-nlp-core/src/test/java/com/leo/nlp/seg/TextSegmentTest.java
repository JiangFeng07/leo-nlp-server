package com.leo.nlp.seg;

import org.junit.Test;

/**
 * Created by lionel on 17/12/20.
 */
public class TextSegmentTest {
    @Test
    public void segment() {
        String text = "这里的辣菜，味道非常的不错，喜欢吃辣的来这里，真的是很不错的哦。辣子对虾，辣的恰到好处，虾肉非常的嫩。。酸菜鱼，鱼肉很嫩滑，汤又是酸酸的，吃起来很开胃呢。红枣杏仁豆腐，豆腐非常的入味。还有口水鸡，够辣，吃的是一身汗，冬天去吃的话，非常适合哦。吃的全身热乎乎的，感觉一定非常的好呢。 服务也绝对";
        System.out.println(TextSegment.parseForDishName(text));
    }
}
