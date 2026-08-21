package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Calificacion;

import java.util.List;
import java.util.Optional;

public interface CalificacionService {
    List<Calificacion> getCalificaciones();
    Optional<Calificacion> getCalificacionById(Long id);
    Optional<Calificacion> getCalificacionByProductoId(Long productoId);
    Calificacion addComentario(Long productoId, Long usuarioId, Integer puntuacion, String comentarioTexto);
}
