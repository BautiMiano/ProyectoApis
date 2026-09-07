package com.uade.EcommerceUniformes.marketplace.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ProductoRequest;

public interface ProductoService {

    Page<Producto> getProductos(Pageable pageable);

    Optional<Producto> getProductoById(Long productoId);

    Page<Producto> getProductosByCategoria(Long categoryId, Pageable pageable);
    
    Page<Producto> searchProductos(String nombre, Pageable pageable);
    
    Page<Producto> getProductosByPrecio(double minPrecio, double maxPrecio, Pageable pageable);

    Producto createProducto(ProductoRequest request);

    void deleteProducto(Long productoId);
}