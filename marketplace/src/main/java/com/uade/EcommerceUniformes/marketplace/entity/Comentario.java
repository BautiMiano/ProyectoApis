package com.uade.EcommerceUniformes.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comentarios")
public class Comentario {

    public Comentario() {
    }

    public Comentario(Usuario usuarioComentario, Calificacion calificacion, Integer calificacionScore, String comentarioProducto) {
        this.usuarioComentario = usuarioComentario;
        this.calificacion = calificacion;
        this.calificacion = calificacion;
        this.calificacionScore = calificacionScore;
        this.comentarioProducto = comentarioProducto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioComentario;

    @ManyToOne
    @JoinColumn(name = "calificacion_id")
    @JsonIgnore
    private Calificacion calificacion;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacionScore; // 1 a 5

    @Column(name = "comentario_producto", columnDefinition = "TEXT")
    private String comentarioProducto;

    public Integer getCalificacion() {
        return calificacionScore;
    }
}
