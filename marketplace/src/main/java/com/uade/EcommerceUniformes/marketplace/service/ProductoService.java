package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ProductoRequest;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

    List<Producto> getProductos();

    Optional<Producto> getProductoById(Long productoId);

    List<Producto> getProductosByCategoria(Long categoryId);

    Producto createProducto(ProductoRequest request);
}