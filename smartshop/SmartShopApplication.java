package com.example.smartshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartShopApplication.class, args);
    }
}
