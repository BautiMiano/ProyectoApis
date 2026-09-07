package com.uade.EcommerceUniformes.marketplace.repository;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoCarrito;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);
    List<Carrito> findByEstadoAndFechaInicioPagoBefore(EstadoCarrito estado, LocalDateTime fecha);
}