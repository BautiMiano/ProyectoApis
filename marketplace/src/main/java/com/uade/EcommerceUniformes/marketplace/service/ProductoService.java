package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> getProductos();
    Optional<Producto> getProductoById(Long id);
    List<Producto> getProductosByCategoria(Long categoryId);
    Producto createProducto(Producto producto);
    Double calcularPrecioFinal(Producto producto);
}
