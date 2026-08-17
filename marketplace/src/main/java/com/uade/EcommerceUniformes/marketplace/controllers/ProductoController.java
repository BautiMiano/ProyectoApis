package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("productos")
public class ProductoController {

    @GetMapping
    public ArrayList<Producto> getProductos() {
        ProductoService productoService = new ProductoService();
        return productoService.getProductos();
    }

    @GetMapping("/{productoId}")
    public Producto getProductoById(@PathVariable int productoId) {
        ProductoService productoService = new ProductoService();
        return productoService.getProductoById(productoId);
    }

    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        ProductoService productoService = new ProductoService();
        return productoService.createProducto(producto);
    }
}
