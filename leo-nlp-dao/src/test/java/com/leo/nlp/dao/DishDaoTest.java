package com.leo.nlp.dao;

import com.leo.nlp.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lionel on 18/1/3.
 */
public class DishDaoTest extends AbstractTest {
    @Autowired
    private DishDao dishDao;

    @Test
    public void test(){
        System.out.println(dishDao.selectByPrimaryKey(1));
    }
}
