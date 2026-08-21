package com.uade.EcommerceUniformes.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Producto {

    public Producto() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column
    private String talle;

    @Enumerated(EnumType.STRING)
    @Column
    private EstadoProducto estado;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    @ManyToOne
    @JoinColumn(name = "descuento_id")
    private Descuento descuentoProducto;

    @Column
    private Integer stock;

    @Column
    private String imagen;
}
