package com.uade.EcommerceUniformes.marketplace.repository;

import com.uade.EcommerceUniformes.marketplace.entity.Comentario;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import java.util.ArrayList;
import java.util.Arrays;

public class ComentarioRepository {

    public ArrayList<Comentario> comentarios = new ArrayList<>(
        Arrays.asList(
            Comentario.builder()
                .producto(Producto.builder().id(2).nombre("Ambo Médico Azul").build())
                .usuarioComentario(Usuario.builder().id(1).nombre("Mariana Lopez").build())
                .calificacion(5)
                .comentarioProducto("Excelente tela el ambo, super cómodo para las guardias.")
                .build(),
            Comentario.builder()
                .producto(Producto.builder().id(2).nombre("Ambo Médico Azul").build())
                .usuarioComentario(Usuario.builder().id(2).nombre("Carlos Mendez").build())
                .calificacion(4)
                .comentarioProducto("El talle M me quedó perfecto. Muy buena confección.")
                .build(),
            Comentario.builder()
                .producto(Producto.builder().id(1).nombre("Chaqueta de Chef").build())
                .usuarioComentario(Usuario.builder().id(3).nombre("Chef Martin").build())
                .calificacion(5)
                .comentarioProducto("La chaqueta resiste muy bien el calor de la cocina.")
                .build()
        )
    );

    public ArrayList<Comentario> getComentarios() {
        return this.comentarios;
    }

    public ArrayList<Comentario> getComentariosByProductoId(int productoId) {
        ArrayList<Comentario> filtrados = new ArrayList<>();
        for (Comentario c : this.comentarios) {
            if (c.getProducto() != null && c.getProducto().getId() == productoId) {
                filtrados.add(c);
            }
        }
        return filtrados;
    }

    public Comentario createComentario(Comentario comentario) {
        this.comentarios.add(comentario);
        return comentario;
    }
}