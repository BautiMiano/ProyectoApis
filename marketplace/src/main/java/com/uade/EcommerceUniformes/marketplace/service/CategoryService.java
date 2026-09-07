package com.uade.EcommerceUniformes.marketplace.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.EcommerceUniformes.marketplace.entity.Category;

public interface CategoryService {

    public Page<Category> getCategories(Pageable pageable);

    public Optional<Category> getCategorById(Long categoryId);

    public Category createCategory(String nombre);

    public void desactivarCategory(Long id);

    public void activarCategory(Long id);

}
