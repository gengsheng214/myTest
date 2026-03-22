package com.gs.swiftcv.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("recipes")
public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "recipe_id", type = IdType.AUTO)
    private Long recipeId;

    private String title;

    private String description;

    private Long categoryId;

    private String difficulty;

    private Integer prepTime;

    private Integer cookTime;

    private Integer totalTime;

    private Integer servings;

    private Integer calories;

    private String coverImage;

    private String videoUrl;

    private String requiredIngredients;

    private String tips;

    private Integer views;

    private Integer favoritesCount;

    private BigDecimal averageRating;

    private Date createdAt;

    private Date updatedAt;
}
