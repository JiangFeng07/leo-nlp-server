package com.leo.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by lionel on 17/11/29.
 */
public class CSDNPageProcessor implements PageProcessor {
    private static final String URL_LIST = "http://blog\\.csdn\\.net/\\w+/article/list/\\d+";
    private static final String URL_POST = "http://blog\\.csdn\\.net/\\w+/article/details/\\d+";
    private Site site = Site
            .me()
            .setDomain("blog.sina.com.cn")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(URL_LIST).match()) {
            List<String> urlPostList = page.getHtml().xpath("//div[@id='article_list']").links().regex(URL_POST).all();
            page.addTargetRequests(urlPostList);
            List<String> urlList = page.getHtml().links().regex(URL_LIST).all();
            page.addTargetRequests(urlList);
        } else {
            String baseUrl = "http://blog.csdn.net/";
            page.putField("title", baseUrl + page.getHtml().xpath("//div[@id=\"article_details\"]//a/@href").toString());
            page.putField("url", page.getHtml().xpath("//div[@id=\"article_details\"]//a/text()").toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("输入参数不够");
            System.exit(0);
        }

        String cmd = args[0];
        String url = String.format("http://blog.csdn.net/%s/article/list/1", cmd);
        Spider.create(new CSDNPageProcessor()).addUrl(url).addPipeline(new FilePipeline()).thread(5).run();
    }

}
