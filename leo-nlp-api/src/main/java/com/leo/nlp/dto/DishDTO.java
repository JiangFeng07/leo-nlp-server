package com.leo.nlp.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by lionel on 18/1/3.
 */
@Data
public class DishDTO {
    private Integer id;

    private String dishName;

    private String source;

    private Date createtime;

    private Date updatetime;
}
