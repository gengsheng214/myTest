package com.gs.swiftcv.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FridgeIngredientSaveRequest {
    private Long userId;
    private Long ingredientId;
    private String ingredientName;
    private String category;
    private BigDecimal quantity;
    private String unit;
    private Date purchaseDate;
    private Date expiryDate;
    private String storageLocation;
    private String notes;
}