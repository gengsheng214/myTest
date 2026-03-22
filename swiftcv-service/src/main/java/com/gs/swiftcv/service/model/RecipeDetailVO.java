package com.gs.swiftcv.service.model;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDetailVO {
    private Long recipeId;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private Integer servings;
    private Integer calories;
    private String coverImage;
    private String tips;
    private List<RecipeIngredientVO> ingredients;
    private List<RecipeStepVO> steps;
}