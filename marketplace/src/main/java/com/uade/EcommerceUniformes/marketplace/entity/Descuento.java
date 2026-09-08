package com.uade.EcommerceUniformes.marketplace.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Data
@Entity
public class Descuento {
    public Descuento() {
    }
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idDescuento;
    @Column
    private double porcentaje;

    @PrePersist
    @PreUpdate
    private void validarPorcentaje() {
        if (porcentaje <= 0 || porcentaje > 100) {
            throw new IllegalArgumentException(
                    "El porcentaje de descuento debe ser mayor a 0 y menor o igual a 100");
        }
    }
}
