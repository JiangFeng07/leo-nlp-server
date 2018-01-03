package com.leo.nlp.dao;

import com.leo.nlp.entity.Dish;

/**
 * Created by lionel on 18/1/3.
 */
public interface DishDao {
    Dish selectByPrimaryKey(Integer id);
}
