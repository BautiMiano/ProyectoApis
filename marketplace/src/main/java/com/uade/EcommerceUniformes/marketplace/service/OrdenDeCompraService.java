package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoOrden;
import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.repository.OrdenDeCompraRepository;

import java.sql.Date;
import java.util.ArrayList;

public class OrdenDeCompraService {
    private OrdenDeCompraRepository ordenRepository = new OrdenDeCompraRepository();
    private CarritoSerivce carritoService = new CarritoSerivce();

    public ArrayList<OrdenDeCompra> getOrdenes() {
        return ordenRepository.getOrdenes();
    }

    public OrdenDeCompra createOrdenFromCarrito(int usuarioId) {
        Carrito carrito = carritoService.getCarritoByUsuarioId(usuarioId);
        if (carrito != null && carrito.getProductos() != null && !carrito.getProductos().isEmpty()) {
            double total = 0;
            for (Producto p : carrito.getProductos()) {
                total += p.getPrecio();
            }

            OrdenDeCompra orden = OrdenDeCompra.builder()
                    .usuarioComprador(carrito.getUsuario())
                    .productos(new ArrayList<>(carrito.getProductos()))
                    .total(total)
                    .estado(EstadoOrden.PENDIENTE)
                    .fechaCompra(new Date(System.currentTimeMillis()))
                    .build();

            // Vaciamos el carrito
            carrito.getProductos().clear();
            return ordenRepository.createOrden(orden);
        }
        return null; // Carrito vacio o no existe
    }
}
