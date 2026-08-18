package com.uade.EcommerceUniformes.marketplace.repository;

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
                .nombre("Chaqueta de Chef")
                .descripcion("Chaqueta blanca de cocina doble botonadura")
                .precio(28000.0)
                .talle("L")
                .stock(15)
                .imagen("http://ejemplo.com/chef.jpg")
                .categoria(Category.builder().id(1).nombre("Gastronomia").build())
                .build(),
            Producto.builder()
                .id(2)
                .nombre("Ambo Médico Azul")
                .descripcion("Ambo elastizado antimanchas")
                .precio(32000.0)
                .talle("M")
                .stock(20)
                .imagen("http://ejemplo.com/ambo.jpg")
                .categoria(Category.builder().id(2).nombre("Salud").build())
                .build(),
            Producto.builder()
                .id(3)
                .nombre("Pantalón Cargo")
                .descripcion("Pantalón reforzado con múltiples bolsillos")
                .precio(35000.0)
                .talle("42")
                .stock(10)
                .imagen("http://ejemplo.com/cargo.jpg")
                .categoria(Category.builder().id(3).nombre("Industria y Mantenimiento").build())
                .build()
        )
    );

    public ArrayList<Producto> getProductos() {
        return this.productos;
    }

    public Producto getProductoById(int productoId) {
        for (Producto p : this.productos) {
            if (p.getId() == productoId) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Producto> getProductosByCategoria(int categoryId) {
        ArrayList<Producto> filtrados = new ArrayList<>();
        for (Producto p : this.productos) {
            if (p.getCategoria() != null && p.getCategoria().getId() == categoryId) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public Producto createProducto(Producto producto) {
        this.productos.add(producto);
        return producto;
    }
}