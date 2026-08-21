package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Calificacion;
import com.uade.EcommerceUniformes.marketplace.entity.Comentario;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.repository.CalificacionRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ComentarioRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalificacionServiceImpl implements CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public List<Calificacion> getCalificaciones() {
        return calificacionRepository.findAll();
    }

    @Override
    public Optional<Calificacion> getCalificacionById(Long id) {
        return calificacionRepository.findById(id);
    }

    @Override
    public Optional<Calificacion> getCalificacionByProductoId(Long productoId) {
        return calificacionRepository.findByProductoId(productoId);
    }

    @Override
    public Calificacion addComentario(Long productoId, Long usuarioId, Integer puntuacion, String comentarioTexto) {
        if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5 estrellas");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Calificacion calificacion = calificacionRepository.findByProductoId(productoId)
                .orElseGet(() -> {
                    Calificacion nuevaCalificacion = new Calificacion(producto);
                    return calificacionRepository.save(nuevaCalificacion);
                });

        Comentario comentario = new Comentario(usuario, calificacion, puntuacion, comentarioTexto);
        comentario.setCalificacion(calificacion);
        comentario.setUsuarioComentario(usuario);
        comentario.setComentarioProducto(comentarioTexto);

        calificacion.getComentarios().add(comentario);
        calificacion.recalcularPromedio();

        return calificacionRepository.save(calificacion);
    }
}
