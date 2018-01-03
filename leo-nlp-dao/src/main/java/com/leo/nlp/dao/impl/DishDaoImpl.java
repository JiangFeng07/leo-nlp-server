package com.leo.nlp.dao.impl;

import com.leo.nlp.dao.DishDao;
import com.leo.nlp.entity.Dish;
import com.leo.nlp.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lionel on 18/1/3.
 */
@Service("dishDao")
public class DishDaoImpl implements DishDao {
    @Autowired
    private DishMapper dishMapper;

    @Override
    public Dish selectByPrimaryKey(Integer id) {
        return dishMapper.selectByPrimaryKey(id);
    }
}
