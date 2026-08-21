package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return this.categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long categoryId){
        return this.categoryRepository.findById(categoryId);
    }

    public Category createCategory(Category category){
        return this.categoryRepository.save(category);
    }

}

    



