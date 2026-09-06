package com.uade.EcommerceUniformes.marketplace;

import com.uade.EcommerceUniformes.marketplace.entity.Rol;
import com.uade.EcommerceUniformes.marketplace.entity.Usuario;
import com.uade.EcommerceUniformes.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.uade.EcommerceUniformes.marketplace.repository.CategoryRepository categoryRepository;

    @Autowired
    private com.uade.EcommerceUniformes.marketplace.repository.ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@uade.edu.ar";
        Optional<Usuario> adminOptional = usuarioRepository.findByMail(adminEmail);
        
        if (adminOptional.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("admin_user");
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setMail(adminEmail);
            admin.setContrasena(passwordEncoder.encode("admin1234"));
            admin.setRolUsuario(Rol.ADMIN);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado exitosamente: " + adminEmail + " / admin1234");
        } else {
            System.out.println("El usuario ADMIN ya existe.");
        }

        if (categoryRepository.count() == 0) {
            com.uade.EcommerceUniformes.marketplace.entity.Category cat = new com.uade.EcommerceUniformes.marketplace.entity.Category("Uniformes");
            categoryRepository.save(cat);
            System.out.println("Categoria inicial creada con ID: " + cat.getId());
        }

        if (productoRepository.count() == 0) {
            com.uade.EcommerceUniformes.marketplace.entity.Category cat = categoryRepository.findAll().get(0);
            com.uade.EcommerceUniformes.marketplace.entity.Producto prod = com.uade.EcommerceUniformes.marketplace.entity.Producto.builder()
                    .nombre("Chomba Colegio San Martin")
                    .descripcion("Chomba blanca talle M")
                    .precio(15000.0)
                    .talle("M")
                    .stock(0) // Creado con stock 0 a proposito para que la prueba de compra sin stock sea directa
                    .estado(com.uade.EcommerceUniformes.marketplace.entity.EstadoProducto.NUEVO)
                    .categoria(cat)
                    .build();
            productoRepository.save(prod);
            System.out.println("Producto inicial (Stock 0) creado con ID: " + prod.getId());
        }
    }
}
