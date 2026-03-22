package com.gs.swiftcv.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_ingredients")
public class UserIngredient implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_ingredient_id", type = IdType.AUTO)
    private Long userIngredientId;

    private Long userId;

    private Long ingredientId;

    private BigDecimal quantity;

    private String unit;

    private Date purchaseDate;

    private Date expiryDate;

    private String storageLocation;

    private String notes;

    private Date createdAt;
}
