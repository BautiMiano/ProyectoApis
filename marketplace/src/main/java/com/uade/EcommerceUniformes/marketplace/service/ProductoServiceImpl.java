package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ProductoRequest;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {


    private final ProductoRepository productoRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getProductoById(Long productoId) {
        return productoRepository.findById(productoId);
    }

    @Override 
    public List<Producto> getProductosByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public Producto createProducto(ProductoRequest request) {
        Category categoria = null;
        if (request.getCategoryId() != null) {
            categoria = categoryRepository.findById(request.getCategoryId()).
            orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + request.getCategoryId()));
        }
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .talle(request.getTalle())
                .stock(request.getStock())
                .imagen(request.getImagen())
                .estado(request.getEstado())
                .categoria(categoria)
                .build();
        return productoRepository.save(producto);
    }


}
