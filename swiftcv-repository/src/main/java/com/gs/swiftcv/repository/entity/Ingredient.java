package com.gs.swiftcv.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("ingredients")
public class Ingredient implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ingredient_id", type = IdType.AUTO)
    private Long ingredientId;

    private String name;

    private String category;

    private String unit;

    private Integer shelfLife;

    private String iconUrl;

    @TableField("is_common")
    private Boolean common;

    private Date createdAt;
}
