package com.leo.nlp.api;

import com.leo.nlp.dto.DishDTO;

/**
 * Created by lionel on 18/1/3.
 */
public interface DishService {
    DishDTO getDishById(Integer id);
}
