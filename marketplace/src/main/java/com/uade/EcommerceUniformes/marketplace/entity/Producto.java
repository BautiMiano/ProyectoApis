package com.uade.EcommerceUniformes.marketplace.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String talle;
    private EstadoProducto estado;
    private Category categoria;
    private Descuento descuentoProducto;
    private int stock;
    private String imagen;

    public Category getCategoria() {
        return this.categoria;
    }

}
