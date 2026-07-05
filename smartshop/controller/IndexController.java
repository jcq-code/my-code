package com.example.smartshop.controller;

import com.example.smartshop.entity.Category;
import com.example.smartshop.entity.Product;
import com.example.smartshop.entity.ProductQuery;
import com.example.smartshop.service.CategoryService;
import com.example.smartshop.service.ProductService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        // 获取总商品数
        PageInfo<Product> allInfo = productService.findByCondition(new ProductQuery(), 1, 1);
        model.addAttribute("productCount", allInfo.getTotal());

        // 精选商品：每个分类取1-2个，共8个
        List<Product> featured = new ArrayList<>();
        // 各分类代表性商品: 笔记本(1), 算法导论(19), 毛巾(26), 跑步鞋(34), 咖啡豆(43), 卫衣(49), 背包(50), 智能手表(7)
        int[] featuredIds = {1, 7, 19, 26, 34, 43, 49, 50};
        for (int id : featuredIds) {
            Product p = productService.findById(id);
            if (p != null) {
                featured.add(p);
            }
        }
        model.addAttribute("latestProducts", featured);

        return "welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
