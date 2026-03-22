package com.gs.swiftcv.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RecipeRecommendationVO {
    private Long recipeId;
    private String title;
    private String categoryName;
    private String coverImage;
    private Integer totalTime;
    private BigDecimal matchRate;
    private String recommendationType;
    private String aiSuggestion;
    private List<String> missingIngredients;
    private List<RecipeIngredientVO> ingredients;
    private List<RecipeStepVO> steps;
}