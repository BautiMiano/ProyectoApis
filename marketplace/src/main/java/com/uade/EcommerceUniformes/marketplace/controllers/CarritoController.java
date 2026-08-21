package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<Carrito> getCarritos() {
        return carritoService.getCarritos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> getCarritoByUsuarioId(@PathVariable Long usuarioId) {
        Optional<Carrito> carrito = carritoService.getCarritoByUsuarioId(usuarioId);
        return carrito.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrito> createCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = carritoService.createCarrito(usuarioId);
        return ResponseEntity
                .created(URI.create("/carritos/usuario/" + usuarioId))
                .body(carrito);
    }

    @PostMapping("/usuario/{usuarioId}/agregar-producto/{productoId}")
    public ResponseEntity<Carrito> addProductoToCarrito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        Carrito carrito = carritoService.addProductoToCarrito(usuarioId, productoId);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/usuario/{usuarioId}/eliminar-producto/{productoId}")
    public ResponseEntity<Carrito> removeProductoFromCarrito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        Carrito carrito = carritoService.removeProductoFromCarrito(usuarioId, productoId);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<Void> clearCarrito(@PathVariable Long usuarioId) {
        carritoService.clearCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
