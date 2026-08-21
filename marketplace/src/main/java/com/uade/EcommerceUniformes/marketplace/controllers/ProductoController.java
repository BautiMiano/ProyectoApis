package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ProductoRequest;
import com.uade.EcommerceUniformes.marketplace.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("productos")
@RequiredArgsConstructor

public class ProductoController {

    private final ProductoService productoService;

    // GET http://localhost:4002/productos
    @GetMapping
    public List<Producto> getProductos() {
        return this.productoService.getProductos();
    }

    // GET http://localhost:4002/productos/1
    @GetMapping("/{productoId}")
    public Producto getProductoById(@PathVariable Long productoId) {
        return this.productoService.getProductoById(productoId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId)); 
    }

    // GET http://localhost:4002/productos/categoria/2
    @GetMapping("/categoria/{categoryId}")
    public List<Producto> getProductosByCategoria(@PathVariable Long categoryId) {
        return this.productoService.getProductosByCategoria(categoryId);
    }

    // POST http://localhost:4002/productos
    @PostMapping
    public Producto createProducto(@RequestBody ProductoRequest request) {
        return this.productoService.createProducto(request);
    }
}