package com.uade.EcommerceUniformes.marketplace.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.entity.Descuento;
import com.uade.EcommerceUniformes.marketplace.entity.DetalleOrden;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoEnvio;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoOrden;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoPago;
import com.uade.EcommerceUniformes.marketplace.entity.ItemCarrito;
import com.uade.EcommerceUniformes.marketplace.entity.OrdenDeCompra;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.entity.dto.OrdenDeCompraRequest;
import com.uade.EcommerceUniformes.marketplace.repository.OrdenDeCompraRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.UsuarioRepository;
import com.uade.EcommerceUniformes.marketplace.repository.CarritoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ItemCarritoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.DescuentoRepository;
import com.uade.EcommerceUniformes.marketplace.exceptions.RecursoNoEncontradoException;
import com.uade.EcommerceUniformes.marketplace.exceptions.StockInsuficienteException;
import com.uade.EcommerceUniformes.marketplace.exceptions.ReglaDeNegocioException;

@Service
public class OrdenDeCompraServiceImpl implements OrdenDeCompraService {

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private ItemCarritoRepository itemCarritoRepository;
    
    @Autowired
    private DescuentoRepository descuentoRepository;

    public List<OrdenDeCompra> getOrdenesDeCompra() {
        return ordenDeCompraRepository.findAll();
    }

    public Optional<OrdenDeCompra> getOrdenDeCompraById(Long ordenId) {
        return ordenDeCompraRepository.findById(ordenId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public OrdenDeCompra createOrdenDeCompra(OrdenDeCompraRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + request.getUsuarioId()));

        Carrito carrito = carritoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ReglaDeNegocioException("El usuario no tiene un carrito activo"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new ReglaDeNegocioException("El carrito está vacío");
        }

        OrdenDeCompra orden = new OrdenDeCompra();
        orden.setUsuario(usuario);
        orden.setFechaCompra(new Date(System.currentTimeMillis()));
        orden.setMetodoDePago(request.getMetodoDePago());
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setEstadoPago(EstadoPago.PENDIENTE_PAGO);
        orden.setEstadoEnvio(EstadoEnvio.EN_PREPARACION);
        orden.setComprobante(request.getComprobante());
        orden.setDireccionEnvio(request.getDireccionEnvio());

        List<DetalleOrden> detalles = new ArrayList<>();
        double total = 0;

        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            
            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("No hay stock suficiente para el producto: " + producto.getNombre());
            }
            
            // Reducir stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
            
            // Crear DetalleOrden con precios históricos
            double subtotalItem = producto.getPrecio() * item.getCantidad();
            DetalleOrden detalle = new DetalleOrden(
                producto.getNombre(), 
                producto.getPrecio(), 
                item.getCantidad(), 
                subtotalItem, 
                producto, 
                orden
            );
            detalles.add(detalle);
            total += subtotalItem;
        }

        if (request.getDescuentoId() != null) {
            Descuento descuento = descuentoRepository.findById(request.getDescuentoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Descuento no encontrado"));
            
            // porcentaje as 10 for 10%
            double descuentoMultiplier = 1.0 - (descuento.getPorcentaje() / 100.0);
            total = total * descuentoMultiplier;
        }
        
        orden.setTotal(total);
        orden.setDetalles(detalles);

        OrdenDeCompra savedOrden = ordenDeCompraRepository.save(orden);

        // Vaciar carrito luego de crear la orden
        itemCarritoRepository.deleteAll(carrito.getItems());
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return savedOrden;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteOrdenDeCompra(Long ordenId) {
        OrdenDeCompra orden = ordenDeCompraRepository.findById(ordenId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Orden de compra no encontrada con id: " + ordenId));

        ordenDeCompraRepository.delete(orden);
    }
}