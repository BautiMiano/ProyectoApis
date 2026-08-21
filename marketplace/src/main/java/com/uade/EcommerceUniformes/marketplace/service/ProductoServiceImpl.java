package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> getProductosByCategoria(Long categoryId) {
        return productoRepository.findByCategoriaId(categoryId);
    }

    @Override
    public Producto createProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Double calcularPrecioFinal(Producto producto) {
        double precio = producto.getPrecio();
        if (producto.getDescuentoProducto() == null) {
            return precio;
        }
        double porcentaje = producto.getDescuentoProducto().getPorcentaje();
        if (porcentaje < 0 || porcentaje > 70) {
            throw new IllegalArgumentException("El descuento debe estar entre 0% y 70%");
        }
        return precio - (precio * porcentaje / 100.0);
    }
}
