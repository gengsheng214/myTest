package com.gs.swiftcv.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("recipe_ingredients")
public class RecipeIngredient implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long recipeId;

    private Long ingredientId;

    private String ingredientName;

    private BigDecimal quantity;

    private String unit;

    private String notes;
}
