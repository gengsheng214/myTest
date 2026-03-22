package com.gs.swiftcv.service.model;

import lombok.Data;

@Data
public class RecipeCardVO {
    private Long recipeId;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private String coverImage;
}