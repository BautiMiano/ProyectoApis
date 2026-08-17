package com.uade.EcommerceUniformes.marketplace.repository;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;

import java.util.ArrayList;

public class CarritoRepository {
    public ArrayList<Carrito> carritos = new ArrayList<>();

    public ArrayList<Carrito> getCarritos() {
        return this.carritos;
    }

    public Carrito getCarritoByUsuarioId(int usuarioId) {
        for (Carrito c : carritos) {
            if (c.getUsuario() != null && c.getUsuario().getId() == usuarioId) {
                return c;
            }
        }
        return null;
    }

    public Carrito createCarrito(Carrito carrito) {
        carrito.setId(carritos.size() + 1);
        if (carrito.getProductos() == null) {
            carrito.setProductos(new ArrayList<>());
        }
        carritos.add(carrito);
        return carrito;
    }

    public Carrito addProductoToCarrito(int usuarioId, Producto producto) {
        Carrito carrito = getCarritoByUsuarioId(usuarioId);
        if (carrito == null) {
            carrito = Carrito.builder().productos(new ArrayList<>()).build();
            // Para simplificar, no le asignamos el usuario complejo, pero idealmente se buscaría
            createCarrito(carrito);
        }
        carrito.getProductos().add(producto);
        return carrito;
    }
}
