package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


public class CategoryService {

    public ArrayList<Category> getCategories(){
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategories();
    }

    public Category getCategorById(@PathVariable int categoryId){
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategoryById(categoryId);
    }

    public Category createCategory(@RequestBody Category categoria){
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.createCategory(categoria);
    }
}
