package com.uade.EcommerceUniformes.marketplace.repository;


import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenDeCompraRepository extends JpaRepository<OrdenDeCompra, Long> {
    List<OrdenDeCompra> findByUsuarioId(Long usuarioId);

    @Query("SELECT DISTINCT o FROM OrdenDeCompra o JOIN o.items i WHERE i.producto.vendedor.id = :vendedorId")
    List<OrdenDeCompra> findByVendedorId(@Param("vendedorId") Long vendedorId);
    }
