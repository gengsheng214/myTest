package com.gs.swiftcv.service.model;

import lombok.Data;

@Data
public class RecipeStepVO {
    private Integer stepNumber;
    private String description;
    private String imageUrl;
    private String tips;
}