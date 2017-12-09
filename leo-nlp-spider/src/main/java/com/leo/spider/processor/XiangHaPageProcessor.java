package com.leo.spider.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lionel on 17/12/7.
 */
@Component
public class XiangHaPageProcessor implements PageProcessor {
    private static final String URL_LIST = "https://www\\.xiangha\\.com/caipu/\\w-\\w+/hot-\\w+/";
    private static final String URL_CAIPIN = "https://www\\.xiangha\\.com/caipu/\\d+\\.html";
    private static final String URL_CAIXI = "https://www\\.xiangha\\.com/caipu/\\w-\\w+/";

    private Site site = Site
            .me()
            .setDomain("www.xiangha.com")
            .setSleepTime(6000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
            .setUserAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");

    @Override
    public void process(Page page) {
        if ((page.getUrl().regex("^https://www\\.xiangha\\.com/caipu/$").match())) {
            List<String> urlDishCategories = page.getHtml().xpath("//div[@class='rec_classify_cell clearfix']").links().regex(URL_CAIXI).all();
            page.addTargetRequests(urlDishCategories.stream().map(e -> String.format("%shot-1/", e)).collect(Collectors.toList()));
        }

        if (page.getUrl().regex(URL_LIST).match()) {
            List<String> urlPostList = page.getHtml().xpath("//div[@class='rec_list']").links().regex(URL_CAIPIN).all();
            page.addTargetRequests(urlPostList);
            List<String> urlList = page.getHtml().links().regex(URL_LIST).all();
            page.addTargetRequests(urlList);
        }
        if (page.getUrl().regex(URL_CAIPIN).match()) {
            String dish = page.getHtml().xpath("//div[@class='rec_content']/h2/text()").toString();
            page.putField("dish", dish);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
