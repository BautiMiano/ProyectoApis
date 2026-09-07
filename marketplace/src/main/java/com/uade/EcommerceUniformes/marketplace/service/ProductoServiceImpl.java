package com.uade.EcommerceUniformes.marketplace.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.entity.dto.ProductoRequest;
import com.uade.EcommerceUniformes.marketplace.repository.CategoryRepository;
import com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository;
import com.uade.EcommerceUniformes.marketplace.repository.UsuarioRepository;
import com.uade.EcommerceUniformes.marketplace.exceptions.RecursoNoEncontradoException;
import com.uade.EcommerceUniformes.marketplace.exceptions.PermisoDenegadoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoryRepository categoryRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Page<Producto> getProductos(Pageable pageable) {
        return productoRepository.findAllByActivoTrue(pageable);
    }

    @Override
    public Optional<Producto> getProductoById(Long productoId) {
        return productoRepository.findByIdAndActivoTrue(productoId);
    }

    @Override
    public Page<Producto> getProductosByCategoria(Long categoriaId, Pageable pageable) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageable);
    }

    @Override
    public Page<Producto> searchProductos(String nombre, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre, pageable);
    }

    @Override
    public Page<Producto> getProductosByPrecio(double minPrecio, double maxPrecio, Pageable pageable) {
        return productoRepository.findByPrecioBetweenAndActivoTrue(minPrecio, maxPrecio, pageable);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Producto createProducto(ProductoRequest request) {
        Producto producto = new Producto();

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setTalle(request.getTalle());
        producto.setStock(request.getStock());
        producto.setEstado(request.getEstado());
        producto.setActivo(true);

        if (request.getCategoryId() != null) {
            Category categoria = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Categoria no encontrada con id: " + request.getCategoryId()
            ));
            producto.setCategoria(categoria);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> vendedorOpt = usuarioRepository.findByMail(username);
        if (vendedorOpt.isPresent()) {
            producto.setVendedor(vendedorOpt.get());
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteProducto(Long productoId) {
        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (producto.getVendedor() != null && !producto.getVendedor().getUsername().equals(username)) {
            throw new PermisoDenegadoException("No tienes permiso para eliminar este producto");
        }

        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
