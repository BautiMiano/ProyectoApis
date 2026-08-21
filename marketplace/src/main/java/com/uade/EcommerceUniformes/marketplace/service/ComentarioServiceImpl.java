package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Comentario;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ComentarioRequest;
import com.uade.EcommerceUniformes.marketplace.repository.ComentarioRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {
    private final ComentarioRepository comentarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public List<Comentario> getComentarios() {
        return comentarioRepository.findAll();
    }

    @Override
    public Optional<Comentario> getComentariosById(Long comentarioId) {
        return comentarioRepository.findById(comentarioId);
    }

    @Override
    public List<Comentario> getComentariosByProductoId(Long productoId) {
        return comentarioRepository.findByProductoId(productoId);
    }

    @Override
    public Comentario createComentario(ComentarioRequest request) {
        Producto producto = null;
        if (request.getProductoId() != null) {
            producto = productoRepository.findById(request.getProductoId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + request.getProductoId()));
        }
        Comentario comentario = Comentario.builder()
                .usuarioId(request.getUsuarioId())
                .comentarioProducto(request.getComentarioProducto())
                .calificacion(request.getCalificacion())
                .producto(producto)
                .build();
        return comentarioRepository.save(comentario);

        
}}
