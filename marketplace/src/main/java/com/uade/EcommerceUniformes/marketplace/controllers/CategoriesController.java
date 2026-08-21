package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import org.springframework.web.bind.annotation.*;
import com.uade.EcommerceUniformes.marketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor

public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories(){
       return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId){// localhost:4002/categories/id // sirve oara pedir una categoria especifica por su id
        return categoryService.getCategoryById(categoryId)
        .orElseThrow(()-> new RuntimeException("Categoria no encontrada con id: " + categoryId));
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category){ //crea una categoria // localhost:4002/categories
        return categoryService.createCategory(category);
    }




}