package com.gs.swiftcv.service.model;

import lombok.Data;

@Data
public class RecipeIngredientVO {
    private String ingredientName;
    private String quantity;
    private String notes;
}