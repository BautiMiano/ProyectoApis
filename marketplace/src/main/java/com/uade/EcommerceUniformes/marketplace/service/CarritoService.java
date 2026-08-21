package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;

import java.util.List;
import java.util.Optional;

public interface CarritoService {
    List<Carrito> getCarritos();
    Optional<Carrito> getCarritoById(Long id);
    Optional<Carrito> getCarritoByUsuarioId(Long usuarioId);
    Carrito createCarrito(Long usuarioId);
    Carrito addProductoToCarrito(Long usuarioId, Long productoId);
    Carrito removeProductoFromCarrito(Long usuarioId, Long productoId);
    void clearCarrito(Long usuarioId);
}
