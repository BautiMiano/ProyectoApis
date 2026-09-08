package com.uade.EcommerceUniformes.marketplace.service;

import com.uade.EcommerceUniformes.marketplace.entity.Carrito;
import com.uade.EcommerceUniformes.marketplace.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CarritoCleanupTask {

    @Autowired
    private CarritoRepository carritoRepository;

    // Lee el valor desde properties. Si no existe, usa 120 minutos (2 horas) por defecto
    @Value("${carrito.vencimiento.minutos:120}")
    private long minutosVencimiento;

    // Se ejecuta cada minuto para comprobar si hay carritos vencidos
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void limpiarCarritosVencidos() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusMinutes(minutosVencimiento);
        
        List<Carrito> carritosVencidos = carritoRepository.findByFechaUltimaModificacionBefore(fechaLimite);
        
        for (Carrito carrito : carritosVencidos) {
            if (carrito.getItems() != null && !carrito.getItems().isEmpty()) {
                System.out.println("Vaciando carrito ID " + carrito.getId() + " por inactividad (Usuario ID " + carrito.getUsuario().getId() + ")");
                carrito.getItems().clear();
                carritoRepository.save(carrito);
            }
        }
    }
}
