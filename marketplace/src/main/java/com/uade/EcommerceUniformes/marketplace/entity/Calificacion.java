package com.uade.EcommerceUniformes.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "calificaciones")
public class Calificacion {

    public Calificacion() {
        this.comentarios = new ArrayList<>();
    }

    public Calificacion(Producto producto) {
        this.producto = producto;
        this.calificacionTotal = 0.0f;
        this.comentarios = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;

    @Column(name = "calificacion_total")
    private Float calificacionTotal = 0.0f;

    @OneToMany(mappedBy = "calificacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("calificacion")
    private List<Comentario> comentarios = new ArrayList<>();

    public void recalcularPromedio() {
        if (comentarios == null || comentarios.isEmpty()) {
            this.calificacionTotal = 0.0f;
        } else {
            double suma = comentarios.stream().mapToDouble(Comentario::getCalificacion).sum();
            this.calificacionTotal = (float) Math.round((suma / comentarios.size()) * 10.0) / 10.0f;
        }
    }
}
