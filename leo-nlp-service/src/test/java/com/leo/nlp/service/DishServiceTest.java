package com.leo.nlp.service;

import com.leo.nlp.AbstractTest;
import com.leo.nlp.api.DishService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lionel on 18/1/3.
 */
public class DishServiceTest extends AbstractTest {
    @Autowired
    private DishService dishService;

    @Test
    public void test() {
        System.out.println(dishService.getDishById(1));
    }
}
