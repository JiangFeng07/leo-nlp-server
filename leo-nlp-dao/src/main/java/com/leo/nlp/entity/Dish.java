package com.leo.nlp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Dish {
    private Integer id;

    private String dishName;

    private String source;

    private Date createtime;

    private Date updatetime;
}
