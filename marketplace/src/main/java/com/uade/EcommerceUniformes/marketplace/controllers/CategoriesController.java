package com.uade.EcommerceUniformes.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.service.CategoryService;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Page<Category> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.getCategories(pageable);
    }

    @GetMapping("/{categoryId}")
    public Optional<Category> getCategorById(@PathVariable Long categoryId){
        return categoryService.getCategorById(categoryId);
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody Category category){
        Category resultado = categoryService.createCategory(category.getNombre());
        return ResponseEntity
                .created(URI.create("/categories/" + resultado.getId()))
                .body(resultado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> desactivarCategory(@PathVariable Long id){
        categoryService.desactivarCategory(id);
        return ResponseEntity.ok("Categoria desactivada");
    }
}