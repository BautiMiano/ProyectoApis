package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Calificacion;
import com.uade.EcommerceUniformes.marketplace.entity.Comentario;
import com.uade.EcommerceUniformes.marketplace.service.CalificacionSerivice;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("calificaciones")
public class CalificacionController {

    @GetMapping
    public ArrayList<Calificacion> getCalificaciones() {
        CalificacionSerivice calificacionService = new CalificacionSerivice();
        return calificacionService.getCalificaciones();
    }

    @GetMapping("/producto/{productoId}")
    public Calificacion getCalificacionByProductoId(@PathVariable int productoId) {
        CalificacionSerivice calificacionService = new CalificacionSerivice();
        return calificacionService.getCalificacionByProductoId(productoId);
    }

    @PostMapping("/producto/{productoId}")
    public Calificacion addComentario(@PathVariable int productoId, @RequestBody Comentario comentario) {
        CalificacionSerivice calificacionService = new CalificacionSerivice();
        return calificacionService.addComentario(productoId, comentario);
    }
}
