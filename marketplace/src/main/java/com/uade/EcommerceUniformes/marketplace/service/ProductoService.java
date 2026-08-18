package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import java.util.ArrayList;

public class ProductoService {

    private ProductoRepository productoRepository = new ProductoRepository();

    public ArrayList<Producto> getProductos() {
        return productoRepository.getProductos();
    }

    public Producto getProductoById(int productoId) {
        return productoRepository.getProductoById(productoId);
    }

    public ArrayList<Producto> getProductosByCategoria(int categoryId) {
        return productoRepository.getProductosByCategoria(categoryId);
    }

    public Producto createProducto(Producto producto) {
        return productoRepository.createProducto(producto);
    }
}