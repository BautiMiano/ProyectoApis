// package com.uade.EcommerceUniformes.marketplace.repository;

<<<<<<< HEAD
import com.uade.EcommerceUniformes.marketplace.entity.Category;
import com.uade.EcommerceUniformes.marketplace.entity.EstadoProducto;
import com.uade.EcommerceUniformes.marketplace.entity.Producto;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductoRepository {
    public ArrayList<Producto> productos = new ArrayList<>(
        Arrays.asList(
            Producto.builder()
                .id(1)
                .nombre("Ambo Médico Clásico")
                .descripcion("Ambo médico unisex color azul marino.")
                .precio(35000)
                .talle("M")
                .estado(EstadoProducto.NUEVO)
                .categoria(Category.builder().id(2).nombre("Salud").build())
                .stock(50)
                .imagen("https://images.unsplash.com/photo-1584515979956-d9f6e5d09982")
                .build(),
            Producto.builder()
                .id(2)
                .nombre("Pantalón Cargo Trabajo")
                .descripcion("Pantalón reforzado para industria.")
                .precio(45000)
                .talle("L")
                .estado(EstadoProducto.NUEVO)
                .categoria(Category.builder().id(3).nombre("Industria y Mantenimiento").build())
                .stock(20)
                .imagen("https://images.unsplash.com/photo-1584515979956-d9f6e5d09982")
                .build()
        )
    );

    public ArrayList<Producto> getProductos() {
        return this.productos;
    }

    public Producto getProductoById(int productoId) {
        for (Producto p : productos) {
            if (p.getId() == productoId) {
                return p;
            }
        }
        return null;
    }

    public Producto createProducto(Producto producto) {
        producto.setId(productos.size() + 1);
        productos.add(producto);
        return producto;
    }
}
=======

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.uade.EcommerceUniformes.marketplace.entity.Producto;

// @Repository
// public interface ProductoRepository extends JpaRepository<Producto,Long> {
// }
>>>>>>> origin/main
