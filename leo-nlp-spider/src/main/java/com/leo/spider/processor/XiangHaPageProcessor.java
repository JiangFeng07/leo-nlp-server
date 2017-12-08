package com.leo.spider.processor;

import com.leo.spider.pipeline.XiangHaPipeline;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by lionel on 17/12/7.
 */
@Component
public class XiangHaPageProcessor implements PageProcessor {
    private static final String URL_LIST = "https://www\\.xiangha\\.com/caipu/x-chuancai/hot-\\w+/";
    private static final String URL_CAIPIN = "https://www\\.xiangha\\.com/caipu/\\d+\\.html";

    private Site site = Site
            .me()
            .setDomain("www.xiangha.com")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(URL_LIST).match()) {
            List<String> urlPostList = page.getHtml().xpath("//div[@class='rec_list']").links().regex(URL_CAIPIN).all();
            page.addTargetRequests(urlPostList);
            List<String> urlList = page.getHtml().links().regex(URL_LIST).all();
            page.addTargetRequests(urlList);
        } else {
            page.putField("dish", page.getHtml().xpath("//div[@class='rec_content']/h2/text()").toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("输入参数不够");
//            System.exit(0);
//        }
//
//        String cmd = args[0];
//        Spider.create(new XiangHaPageProcessor()).addUrl(cmd).addPipeline(new XiangHaPipeline()).run();
//    }

}
