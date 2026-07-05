package com.example.smartshop.entity;

import lombok.Data;

@Data
public class ProductQuery {
    private Integer catId;
    private String keyword;
    private Double minPrice;
    private Double maxPrice;
}
