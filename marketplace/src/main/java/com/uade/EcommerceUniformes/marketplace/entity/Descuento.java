package com.uade.EcommerceUniformes.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Descuento {

    public Descuento() {
    }

    public Descuento(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double porcentaje;
}
