package com.uade.EcommerceUniformes.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.entity.ItemCarrito;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.repository.CarritoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ItemCarritoRepository;
import com.uade.EcommerceUniformes.marketplace.exceptions.RecursoNoEncontradoException;
import com.uade.EcommerceUniformes.marketplace.exceptions.StockInsuficienteException;
import com.uade.EcommerceUniformes.marketplace.exceptions.ReglaDeNegocioException;

@Service
public class CarritoServiceImpl implements CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    public List<Carrito> getCarritos() {
        return carritoRepository.findAll();
    }

    public Optional<Carrito> getCarritoById(Long carritoId) {
        return carritoRepository.findById(carritoId);
    }

    public Optional<Carrito> getCarritoByUsuarioId(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Carrito createCarrito(Long usuarioId) {
        if (carritoRepository.findByUsuarioId(usuarioId).isPresent())
            throw new ReglaDeNegocioException("El usuario ya tiene un carrito creado");

        Usuario usuario = usuarioService.getUsuarioById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId));

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        return carritoRepository.save(carrito);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Carrito addProductoToCarrito(Long carritoId, Long productoId, int cantidad) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));

        Producto producto = productoService.getProductoById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));
                
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay stock suficiente para el producto: " + producto.getNombre());
        }

        Optional<ItemCarrito> itemExistente =
                itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId);

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            if (producto.getStock() < item.getCantidad() + cantidad) {
                throw new StockInsuficienteException("No hay stock suficiente para el producto: " + producto.getNombre());
            }
            item.setCantidad(item.getCantidad() + cantidad);
            itemCarritoRepository.save(item);
        } else {
            if (carrito.getItems() == null)
                carrito.setItems(new ArrayList<>());

            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            carrito.getItems().add(nuevoItem);
        }

        return carritoRepository.save(carrito);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Carrito updateCantidadProducto(Long carritoId, Long productoId, int cantidad) {
        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto no se encuentra en el carrito"));

        item.setCantidad(cantidad);
        itemCarritoRepository.save(item);

        return carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Carrito removeProductoFromCarrito(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));

        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto no se encuentra en el carrito"));

        carrito.getItems().remove(item);
        itemCarritoRepository.delete(item);
        return carritoRepository.save(carrito);
    }
    
    @Transactional(rollbackFor = Throwable.class)
    public void vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));
        
        itemCarritoRepository.deleteAll(carrito.getItems());
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }
}