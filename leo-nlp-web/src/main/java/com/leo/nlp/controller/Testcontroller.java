package com.leo.nlp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lionel on 18/1/5.
 */
@RestController
public class TestController {

    @RequestMapping(value = "/hello")
    @ResponseBody
    public String show() {
        return "hello";
    }
}
