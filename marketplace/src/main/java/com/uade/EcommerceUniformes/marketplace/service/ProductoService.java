package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;

import java.util.ArrayList;

public class ProductoService {
    private ProductoRepository productoRepository = new ProductoRepository();

    public ArrayList<Producto> getProductos() {
        return productoRepository.getProductos();
    }

    public Producto getProductoById(int id) {
        return productoRepository.getProductoById(id);
    }

    public Producto createProducto(Producto producto) {
        return productoRepository.createProducto(producto);
    }
}
