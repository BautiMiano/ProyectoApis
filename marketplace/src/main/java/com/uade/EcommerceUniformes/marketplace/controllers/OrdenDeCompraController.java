package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.service.OrdenDeCompraService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("ordenes")
public class OrdenDeCompraController {

    @GetMapping
    public ArrayList<OrdenDeCompra> getOrdenes() {
        OrdenDeCompraService ordenService = new OrdenDeCompraService();
        return ordenService.getOrdenes();
    }

    @PostMapping("/checkout/{usuarioId}")
    public OrdenDeCompra createOrdenFromCarrito(@PathVariable int usuarioId) {
        OrdenDeCompraService ordenService = new OrdenDeCompraService();
        return ordenService.createOrdenFromCarrito(usuarioId);
    }
}
