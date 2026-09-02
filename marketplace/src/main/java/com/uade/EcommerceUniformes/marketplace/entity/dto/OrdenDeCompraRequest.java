package com.uade.EcommerceUniformes.marketplace.entity.dto;

import com.uade.EcommerceUniformes.marketplace.entity.MetodoDePago;

import lombok.Data;

@Data
public class OrdenDeCompraRequest {
    private Long usuarioId;

    private String comprobante;

    private MetodoDePago metodoDePago;
}