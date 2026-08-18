package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Calificacion;
import com.uade.EcommerceUniformes.marketplace.entity.Comentario;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.repository.CalificacionRepository;

import java.util.ArrayList;

public class CalificacionSerivice {
    private CalificacionRepository calificacionRepository = new CalificacionRepository();
    private ProductoService productoService = new ProductoService();

    public ArrayList<Calificacion> getCalificaciones() {
        return calificacionRepository.getCalificaciones();
    }

    public Calificacion getCalificacionByProductoId(int productoId) {
        return calificacionRepository.getCalificacionByProductoId(productoId);
    }

    public Calificacion addComentario(int productoId, Comentario comentario) {
        Producto producto = productoService.getProductoById(productoId);
        if (producto != null) {
            return calificacionRepository.addComentario(producto, comentario);
        }
        return null;
    }
}
