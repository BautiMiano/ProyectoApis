package com.uade.EcommerceUniformes.marketplace.repository;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoriaId(Long categoriaId);

    Page<Producto> findAllByActivoTrue(Pageable pageable);
    
    Page<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);
    
    Page<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);
    
    Page<Producto> findByPrecioBetweenAndActivoTrue(double minPrecio, double maxPrecio, Pageable pageable);

    Optional<Producto> findByIdAndActivoTrue(Long id);
}