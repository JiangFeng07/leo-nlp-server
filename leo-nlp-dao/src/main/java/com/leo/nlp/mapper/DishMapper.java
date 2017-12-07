package com.leo.nlp.mapper;

import com.leo.nlp.entity.Dish;

public interface DishMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dish record);

    int insertSelective(Dish record);

    Dish selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dish record);

    int updateByPrimaryKey(Dish record);
}