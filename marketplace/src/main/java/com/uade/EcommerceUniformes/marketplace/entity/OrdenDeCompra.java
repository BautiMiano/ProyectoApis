package com.uade.EcommerceUniformes.marketplace.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class OrdenDeCompra {

    public OrdenDeCompra() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date fechaCompra;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<DetalleOrden> detalles;

    @Column(nullable = false)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoDePago metodoDePago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE_PAGO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estadoEnvio = EstadoEnvio.EN_PREPARACION;

    @Embedded
    private Direccion direccionEnvio;

    @Column
    private String comprobante;
}
