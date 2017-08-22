package com.leo.nlp.controller;

import com.leo.nlp.service.TextSuggestService;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.pinyin.Pinyin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lionel on 17/8/22.
 */
@RestController
public class TextSuggestController {
    @Autowired
    private TextSuggestService textSuggestService;

    @GetMapping("/suggests")
    public String suggests(@RequestParam("text") String text) {
        String spell = StringUtils.join(Pinyin.pinyin(text), "");
        List<String> list = textSuggestService.suggest(spell);
        if (list.size() > 0) {
            return list.get(0);
        }
        return "Leo";
    }
}
