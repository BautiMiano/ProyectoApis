package com.uade.EcommerceUniformes.marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  //estos dos los usamos para que spring boot pueda recibir
@AllArgsConstructor // peticiones post y asi no tira error

public class Comentario {
    private Usuario usuarioComentario;
    private Producto producto;
    private int calificacion;
    private String comentarioProducto;

    public Producto getProducto() {
        return this.producto;
    }
}
