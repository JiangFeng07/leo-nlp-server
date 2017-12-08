package com.leo.spider.processor;

import com.leo.nlp.entity.Dish;
import com.leo.nlp.mapper.DishMapper;
import com.leo.spider.AbstractTest;
import com.leo.spider.pipeline.XiangHaPipeline;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;

/**
 * Created by lionel on 17/12/8.
 */
public class XiangHaProcessorTest extends AbstractTest {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private XiangHaPageProcessor xiangHaPageProcessor;

    @Autowired
    private XiangHaPipeline xiangHaPipeline;

    @Test
    public void test() {
        Dish dish = new Dish();
        dish.setDishName("西红柿炒鸡蛋");
        dish.setSource("香哈网");
        dishMapper.insert(dish);
    }

    @Test
    public void test2() {
        String cmd = "https://www.xiangha.com/caipu/x-chuancai/hot-1/";
        Spider.create(xiangHaPageProcessor).addUrl(cmd).addPipeline(xiangHaPipeline).thread(5).run();
    }
}
