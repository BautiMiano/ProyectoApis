package com.uade.EcommerceUniformes.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.entity.dto.OrdenDeCompraRequest;

public interface OrdenDeCompraService {

    public Optional<OrdenDeCompra> getOrdenDeCompraById(Long ordenId, Long usuarioId);

    public OrdenDeCompra createOrdenDeCompra(OrdenDeCompraRequest request);

    public void deleteOrdenDeCompra(Long ordenId);

    public List<OrdenDeCompra> getOrdenesDeCompraParaUsuario(Long usuarioId);
}