package com.gs.swiftcv.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("recipe_steps")
public class RecipeStep implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "step_id", type = IdType.AUTO)
    private Long stepId;

    private Long recipeId;

    private Integer stepNumber;

    private String description;

    private String imageUrl;

    private Integer timerDuration;

    private String tips;
}
