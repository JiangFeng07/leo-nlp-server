package com.leo.spider.processor;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 18/5/17.
 */
@Component
public class AnalogySentenceProcessor implements PageProcessor {

    private static final String URL_LIST = "http://www\\.zaojuzi\\.com/zuowen/list_\\d+_\\d+.html";
    private static final String URL_POST = "/\\w+/\\d+\\.html";
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    public static List<String> sentences = new ArrayList<>();


    @Override
    public void process(Page page) {
        if (page.getUrl().regex(URL_LIST).match()) {
            List<String> urlPostList = page.getHtml().xpath("//div[@class='list']").links().regex(URL_POST).all();
//            urlPostList = urlPostList.stream().map(line -> "http://www.zaojuzi.com" + line).collect(Collectors.toList());
            page.addTargetRequests(urlPostList);
            List<String> urlList = page.getHtml().links().regex(URL_LIST).all();
            page.addTargetRequests(urlList);
        } else {
            String str = page.getHtml().xpath("//div[@class='mboxb']").get();
            Document doc = Jsoup.parse(str);
            Elements elements = doc.getElementsByTag("p");
            for (Element element : elements) {
//                String[] fields = element.text().split("[、|.]");
//                if (fields.length != 2) {
//                    continue;
//                }
                sentences.add(String.format("%s\t%s", page.getUrl(), element.text()));
            }
        }
    }

    private boolean isDigit(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        for (char ch : str.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void write(String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));
            for (String ele : sentences) {
                writer.write(ele + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
