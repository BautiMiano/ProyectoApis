package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import org.springframework.web.bind.annotation.*;
import com.uade.EcommerceUniformes.marketplace.service.CategoryService;

import java.util.ArrayList;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @GetMapping
    public ArrayList<Category> getCategories(){
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategorById(@PathVariable int categoryId){// localhost:4002/categories/id // sirve oara pedir una categoria especifica por su id
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategorById(categoryId);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category categoria){ //crea una categoria // localhost:4002/categories
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(categoria);
    }


}