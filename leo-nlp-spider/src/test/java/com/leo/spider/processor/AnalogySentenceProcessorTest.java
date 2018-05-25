package com.leo.spider.processor;

import com.leo.spider.AbstractTest;
import com.leo.spider.pipeline.XiangHaPipeline;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;

/**
 * Created by lionel on 18/5/18.
 */
public class AnalogySentenceProcessorTest extends AbstractTest {
    @Autowired
    private XiangHaPipeline xiangHaPipeline;

    @Autowired
    private AnalogySentenceProcessor analogySentenceProcessor;

    @Test
    public void test() {
        String url = "http://www.zaojuzi.com/zuowen/list_20_1.html";
        Spider.create(analogySentenceProcessor).addUrl(url).thread(5).run();
        analogySentenceProcessor.write("/tmp/4.csv");
    }
}
