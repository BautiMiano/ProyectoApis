package com.uade.EcommerceUniformes.marketplace.controllers;

import com.uade.EcommerceUniformes.marketplace.entity.Calificacion;
import com.uade.EcommerceUniformes.marketplace.service.CalificacionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @GetMapping
    public List<Calificacion> getCalificaciones() {
        return calificacionService.getCalificaciones();
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Calificacion> getCalificacionByProductoId(@PathVariable Long productoId) {
        Optional<Calificacion> calificacion = calificacionService.getCalificacionByProductoId(productoId);
        return calificacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/producto/{productoId}")
    public ResponseEntity<Calificacion> addComentario(
            @PathVariable Long productoId,
            @RequestBody ComentarioRequestDto request) {

        Calificacion calificacion = calificacionService.addComentario(
                productoId,
                request.getUsuarioId(),
                request.getCalificacion(),
                request.getComentarioProducto()
        );
        return ResponseEntity.ok(calificacion);
    }

    @Data
    public static class ComentarioRequestDto {
        private Long usuarioId;
        private Integer calificacion; // 1 a 5
        private String comentarioProducto;
    }
}
