package com.gs.swiftcv.service.model;

import lombok.Data;

@Data
public class KitchenCategoryVO {
    private Long categoryId;
    private String name;
    private String description;
    private String iconUrl;
}