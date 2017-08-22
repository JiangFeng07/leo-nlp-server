package com.leo.nlp.controller;

import com.leo.nlp.service.TextSuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lionel on 17/8/22.
 */
@RestController
@RequestMapping("/text")
public class TextSuggestController {
    @Autowired
    private TextSuggestService textSuggestService;

    @GetMapping("/suggests")
    public String suggests(@RequestParam("text") String text) {
        return textSuggestService.suggest(text);
    }
}
