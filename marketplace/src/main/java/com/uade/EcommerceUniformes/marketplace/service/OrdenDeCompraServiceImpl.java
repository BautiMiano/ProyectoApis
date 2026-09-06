package com.uade.EcommerceUniformes.marketplace.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.EcommerceUniformes.marketplace.entity.EstadoOrden;
import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.entity.dto.OrdenDeCompraRequest;
import com.uade.EcommerceUniformes.marketplace.repository.OrdenDeCompraRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.UsuarioRepository;

@Service
public class OrdenDeCompraServiceImpl implements OrdenDeCompraService {

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<OrdenDeCompra> getOrdenesDeCompra() {
        return ordenDeCompraRepository.findAll();
    }

    public Optional<OrdenDeCompra> getOrdenDeCompraById(Long ordenId) {
        return ordenDeCompraRepository.findById(ordenId);
    }

    @Autowired
    private com.uade.EcommerceUniformes.marketplace.repository.CarritoRepository carritoRepository;

    public OrdenDeCompra createOrdenDeCompra(OrdenDeCompraRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con id: " + request.getUsuarioId()));

        java.util.List<Producto> productos = new java.util.ArrayList<>();
        double total = 0;

        for (Long pId : request.getProductosIds()) {
            Producto producto = productoRepository.findById(pId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + pId));
            
            if (producto.getStock() < 1) {
                throw new RuntimeException("No hay stock suficiente para el producto: " + producto.getNombre());
            }
            
            producto.setStock(producto.getStock() - 1);
            productoRepository.save(producto);
            
            productos.add(producto);
            total += producto.getPrecio();
        }

        OrdenDeCompra orden = new OrdenDeCompra();

        orden.setUsuario(usuario);
        orden.setFechaCompra(new Date(System.currentTimeMillis()));
        orden.setProductos(productos);
        orden.setTotal(total);
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setComprobante(request.getComprobante());
        orden.setMetodoDePago(request.getMetodoDePago());

        OrdenDeCompra savedOrden = ordenDeCompraRepository.save(orden);

        carritoRepository.findByUsuarioId(usuario.getId()).ifPresent(carrito -> {
            carrito.getItems().clear();
            carritoRepository.save(carrito);
        });

        return savedOrden;
    }

    public void deleteOrdenDeCompra(Long ordenId) {

        OrdenDeCompra orden = ordenDeCompraRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException(
                        "Orden de compra no encontrada con id: " + ordenId));

        ordenDeCompraRepository.delete(orden);
    }
}