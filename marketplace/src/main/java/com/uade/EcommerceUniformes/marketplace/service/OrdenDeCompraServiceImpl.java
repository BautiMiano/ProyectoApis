package com.uade.EcommerceUniformes.marketplace.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoOrden;
import com.uade.EcommerceUniformes.marketplace.entity.ItemCarrito;
import com.uade.EcommerceUniformes.marketplace.entity.ItemDeOrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.entity.dto.OrdenDeCompraRequest;
import com.uade.EcommerceUniformes.marketplace.repository.OrdenDeCompraRepository;
import com.uade.EcommerceUniformes.marketplace.repository.UsuarioRepository;

@Service
public class OrdenDeCompraServiceImpl implements OrdenDeCompraService {

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoService carritoService;

    public List<OrdenDeCompra> getOrdenesDeCompra() {
        return ordenDeCompraRepository.findAll();
    }

    public Optional<OrdenDeCompra> getOrdenDeCompraById(Long ordenId) {
        return ordenDeCompraRepository.findById(ordenId);
    }

    public OrdenDeCompra createOrdenDeCompra(OrdenDeCompraRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con id: " + request.getUsuarioId()));

        Carrito carrito = carritoService.getCarritoByUsuarioId(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException(
                        "El usuario no tiene un carrito"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        OrdenDeCompra orden = new OrdenDeCompra();
        orden.setUsuario(usuario);
        orden.setFechaCompra(new Date(System.currentTimeMillis()));
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setComprobante(request.getComprobante());
        orden.setMetodoDePago(request.getMetodoDePago());

        List<ItemDeOrdenDeCompra> items = new ArrayList<>();
        double total = 0;

        for (ItemCarrito itemCarrito : carrito.getItems()) {
            ItemDeOrdenDeCompra item = new ItemDeOrdenDeCompra(
                    orden,
                    itemCarrito.getProducto(),
                    itemCarrito.getCantidad(),
                    itemCarrito.getPrecioUnitario()
            );
            items.add(item);
            total += item.getCantidad() * item.getPrecioUnitario();
        }

        orden.setItems(items);
        orden.setTotal(total);

        OrdenDeCompra ordenGuardada = ordenDeCompraRepository.save(orden);

        carritoService.vaciarCarrito(carrito.getId());

        return ordenGuardada;
    }

    public void deleteOrdenDeCompra(Long ordenId) {

        OrdenDeCompra orden = ordenDeCompraRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException(
                        "Orden de compra no encontrada con id: " + ordenId));

        ordenDeCompraRepository.delete(orden);
    }
}