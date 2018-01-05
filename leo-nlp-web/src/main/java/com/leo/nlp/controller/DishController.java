package com.leo.nlp.controller;

import com.leo.nlp.api.DishService;
import com.leo.nlp.dto.DishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by lionel on 18/1/5.
 */
@Controller
public class DishController {
    @Autowired
    private DishService dishService;

    @RequestMapping(value = "/dishes/{id}", method = RequestMethod.GET)
    @ResponseBody
    public DishDTO showDish(@PathVariable("id") Integer id) {
        return dishService.getDishById(id);
    }
}
