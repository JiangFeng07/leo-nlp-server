package com.leo.nlp.service.impl;

import com.leo.nlp.api.DishService;
import com.leo.nlp.dao.DishDao;
import com.leo.nlp.dto.DishDTO;
import com.leo.nlp.entity.Dish;
import com.leo.nlp.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lionel on 18/1/3.
 */
@Service("dishService")
public class DishServiceImpl implements DishService {
    @Autowired
    private DishDao dishDao;

    @Override
    public DishDTO getDishById(Integer id) {
        if (id == null || id <= 0) {
            return new DishDTO();
        }
        Dish dish = dishDao.selectByPrimaryKey(id);
        return convert(dish);
    }

    private DishDTO convert(Dish dish) {
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish, dishDTO);
        return dishDTO;
    }
}
