package com.leo.spider.pipeline;

import com.leo.nlp.entity.Dish;
import com.leo.nlp.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * Created by lionel on 17/12/8.
 */
@Component
public class XiangHaPipeline implements Pipeline {
    @Autowired
    private DishMapper dishMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            Dish dish = new Dish();
            dish.setDishName(entry.getValue() + "");
            dish.setSource("香哈网");
            dishMapper.insert(dish);
        }
    }
}
