package com.uade.EcommerceUniformes.marketplace.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ProductoRequest;
import com.uade.EcommerceUniformes.marketplace.service.ProductoService;
import com.uade.EcommerceUniformes.marketplace.exceptions.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // GET http://localhost:4002/productos
    @GetMapping
    public Page<Producto> getProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.productoService.getProductos(pageable);
    }

    // GET http://localhost:4002/productos/1
    @GetMapping("/{productoId}")
    public Producto getProductoById(@PathVariable Long productoId) {
        return this.productoService.getProductoById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));
    }

    // GET http://localhost:4002/productos/categoria/2
    @GetMapping("/categoria/{categoryId}")
    public Page<Producto> getProductosByCategoria(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.productoService.getProductosByCategoria(categoryId, pageable);
    }
    
    // GET http://localhost:4002/productos/buscar?nombre=Remera
    @GetMapping("/buscar")
    public Page<Producto> searchProductos(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.productoService.searchProductos(nombre, pageable);
    }

    // GET http://localhost:4002/productos/precio?min=1000&max=5000
    @GetMapping("/precio")
    public Page<Producto> getProductosByPrecio(
            @RequestParam double min,
            @RequestParam double max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.productoService.getProductosByPrecio(min, max, pageable);
    }

    // POST http://localhost:4002/productos
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoRequest request) {
        Producto resultado = this.productoService.createProducto(request);
        return ResponseEntity
                .created(URI.create("/productos/" + resultado.getId()))
                .body(resultado);
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long productoId) {
        productoService.deleteProducto(productoId);
        return ResponseEntity.noContent().build();
    }
}
