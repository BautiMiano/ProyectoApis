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
            admin.setRol(Rol.ADMIN);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN creado exitosamente: " + adminEmail + " / admin1234");
        } else {
            System.out.println("El usuario ADMIN ya existe.");
        }
    }
}
