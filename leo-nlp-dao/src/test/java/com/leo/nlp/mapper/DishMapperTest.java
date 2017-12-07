package com.leo.nlp.mapper;

import com.leo.nlp.AbstractTest;
import com.leo.nlp.entity.Dish;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lionel on 17/12/7.
 */
public class DishMapperTest extends AbstractTest {
    @Autowired
    private DishMapper dishMapper;

    @Test
    public void test() {
        Dish dish = new Dish();
        dish.setDishName("酸菜鱼");
        dish.setSource("香哈网");
        dishMapper.insert(dish);
    }
}
