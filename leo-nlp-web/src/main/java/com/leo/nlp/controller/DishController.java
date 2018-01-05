package com.leo.nlp.controller;

import com.leo.nlp.api.DishService;
import com.leo.nlp.dto.DishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lionel on 18/1/3.
 */
@RestController
@RequestMapping(value = "/dishes")
public class DishController {
    @Autowired
    private DishService dishService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DishDTO show(@PathVariable("id") Integer id) {
        return dishService.getDishById(id);
    }
}
