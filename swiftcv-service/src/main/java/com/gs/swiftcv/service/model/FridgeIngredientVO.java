package com.gs.swiftcv.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FridgeIngredientVO {
    private Long userIngredientId;
    private Long ingredientId;
    private String ingredientName;
    private String category;
    private BigDecimal quantity;
    private String unit;
    private Date purchaseDate;
    private Date expiryDate;
    private String storageLocation;
    private String notes;
    private String iconUrl;
}