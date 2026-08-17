package com.uade.EcommerceUniformes.marketplace.repository;

import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;

import java.util.ArrayList;

public class OrdenDeCompraRepository {
    public ArrayList<OrdenDeCompra> ordenes = new ArrayList<>();

    public ArrayList<OrdenDeCompra> getOrdenes() {
        return this.ordenes;
    }

    public OrdenDeCompra createOrden(OrdenDeCompra orden) {
        orden.setId(ordenes.size() + 1);
        ordenes.add(orden);
        return orden;
    }
}
