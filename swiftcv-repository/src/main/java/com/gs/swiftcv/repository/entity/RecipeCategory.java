package com.gs.swiftcv.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("recipe_categories")
public class RecipeCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    private Long parentId;

    private String name;

    private String description;

    private String iconUrl;

    private Integer sortOrder;
}
