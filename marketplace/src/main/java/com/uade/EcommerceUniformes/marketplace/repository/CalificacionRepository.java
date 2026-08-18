package com.uade.EcommerceUniformes.marketplace.repository;

import com.uade.EcommerceUniformes.marketplace.entity.Calificacion;
import com.uade.EcommerceUniformes.marketplace.entity.Comentario;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;

import java.util.ArrayList;

public class CalificacionRepository {
    public ArrayList<Calificacion> calificaciones = new ArrayList<>();

    public ArrayList<Calificacion> getCalificaciones() {
        return this.calificaciones;
    }

    public Calificacion getCalificacionByProductoId(int productoId) {
        for (Calificacion c : calificaciones) {
            if (c.getProducto() != null && c.getProducto().getId() == productoId) {
                return c;
            }
        }
        return null;
    }

    public Calificacion addComentario(Producto producto, Comentario comentario) {
        Calificacion calificacion = getCalificacionByProductoId(producto.getId());
        if (calificacion == null) {
            calificacion = Calificacion.builder()
                    .producto(producto)
                    .calificacionTotal(0)
                    .comentarios(new ArrayList<>())
                    .build();
            calificaciones.add(calificacion);
        }

        calificacion.getComentarios().add(comentario);

        // Recalcular promedio de estrellas
        int suma = 0;
        for (Comentario com : calificacion.getComentarios()) {
            suma += com.getCalificacion();
        }
        calificacion.setCalificacionTotal(suma / calificacion.getComentarios().size());

        return calificacion;
    }
}
