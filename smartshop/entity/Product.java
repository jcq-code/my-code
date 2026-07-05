package com.example.smartshop.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String photoUrl;
    private Double price;
    private String descp;
    private LocalDate releaseDate;
    private Integer catId;
    private String catName;
}
