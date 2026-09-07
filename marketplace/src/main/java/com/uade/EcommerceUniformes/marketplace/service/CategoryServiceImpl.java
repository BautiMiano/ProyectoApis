package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.repository.CategoryRepository;
import com.uade.EcommerceUniformes.marketplace.exceptions.RecursoNoEncontradoException;
import com.uade.EcommerceUniformes.marketplace.exceptions.ReglaDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getCategories(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }

    public Optional<Category> getCategorById(Long categoryId){
        return categoryRepository.findById(categoryId);
    }

    public Category createCategory(String nombre){
        if (categoryRepository.existsByNombre(nombre)) {
            throw new ReglaDeNegocioException("La categoria que se intenta agregar ya esta creada");
        }
        return categoryRepository.save(new Category(nombre));
    }

    public void desactivarCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada con id: " + id));

        if (!category.getActivo()) {
            throw new ReglaDeNegocioException("La categoria con id: " + id + " ya se encuentra desactivada");
        }
        category.setActivo(false);
        categoryRepository.save(category);
    }

    public void activarCategory(Long id){
        Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("La categoria con id: "+ id + " no se encuentra"));

        if (category.getActivo()) {
            throw new ReglaDeNegocioException("La categoria con id: " + id + " ya se encuentra activada");
        }
        category.setActivo(true);    
        categoryRepository.save(category);
    }
}
