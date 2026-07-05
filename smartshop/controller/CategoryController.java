package com.example.smartshop.controller;

import com.example.smartshop.entity.Category;
import com.example.smartshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "category/form";
    }

    @PostMapping("/save")
    public String save(Category category) {
        categoryService.save(category);
        return "redirect:/category/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return "redirect:/category/list";
    }
}
