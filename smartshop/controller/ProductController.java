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
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(ProductQuery query,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize,
                       Model model) {
        PageInfo<Product> pageInfo = productService.findByCondition(query, pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("query", query);
        return "product/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @PostMapping("/save")
    public String save(Product product) {
        productService.save(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productService.deleteById(id);
        return "redirect:/product/list";
    }
}
