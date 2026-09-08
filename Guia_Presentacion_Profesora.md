# Guía Maestra Paso a Paso - Defensa y Presentación del Marketplace

Esta guía está diseñada para seguir el **flujo completo de la aplicación** en vivo durante la defensa/presentación. Abarca desde levantar el proyecto, registrar y modificar usuarios, crear el catálogo, gestionar el carrito y descuentos, hasta simular errores de stock y concretar una orden de compra exitosa. En cada etapa se incluyen las **preguntas clave de la profesora** con las respuestas técnicas exactas.

---

## 🚀 Fase 0: Puesta en Marcha y Seed Data

### 1. Iniciar la Aplicación
1. Abrir una terminal en la carpeta raíz del backend:
   ```bash
   cd c:\Users\Francosaga\Desktop\TPO_API_OTRO\marketplace
   mvn spring-boot:run
   ```
2. Verificar en la consola de Spring Boot que se ejecutó el `DataSeeder`:
   - Se crea el usuario **ADMIN**: `admin@uade.edu.ar` con contraseña `admin1234`.
   - Se crea la categoría inicial **"Uniformes"** (ID: 1).
   - Se crea un producto inicial con **Stock 0** (ID: 1, *"Chomba Colegio San Martin"*) para probar el control de stock en compras.

> 🗣️ **Pregunta de la Profesora:** *"¿Cómo inicializan los datos de prueba o el usuario administrador en el sistema?"*  
> **Tu Respuesta:** *"Implementamos `CommandLineRunner` en la clase `DataSeeder`. Al arrancar el contexto de Spring, verifica si la base de datos está vacía y genera los registros base de manera idempotente usando `PasswordEncoder` para almacenar los hashes de contraseñas de forma segura."*

---

## 👥 Fase 1: Autenticación, Usuarios y Permisos

### 2. Registrar un Nuevo Usuario (Cliente)
- **Método:** `POST`
- **URL:** `http://localhost:4002/auth/register`
- **Body (JSON):**
  ```json
  {
    "nombreUsuario": "juan_perez",
    "nombre": "Juan",
    "apellido": "Perez",
    "mail": "juan@uade.edu.ar",
    "contrasena": "juan1234"
  }
  ```
- **Respuesta esperada:** `200 OK` con el token JWT (`{"token": "eyJhbGciOi..."}`).

> 🗣️ **Pregunta de la Profesora:** *"Al consultar o registrar un usuario, ¿la API expone el password?"*  
> **Tu Respuesta:** *"No. En la entidad `Usuario`, el atributo `contrasena` tiene la anotación `@JsonIgnore`. Jackson la descarta en el proceso de serialización a JSON, evitando cualquier fuga de credenciales."*

### 3. Login de Usuarios (Obtener Tokens)
- **Método:** `POST`
- **URL:** `http://localhost:4002/auth/authenticate`
- **Body:**
  - Login como **Admin**: `{"mail": "admin@uade.edu.ar", "contrasena": "admin1234"}`
  - Login como **Cliente**: `{"mail": "juan@uade.edu.ar", "contrasena": "juan1234"}`
- Copiá el token del cliente o admin para usarlo en el tab **Authorization -> Bearer Token** de las siguientes peticiones.

### 4. Listar Usuarios y Obtener Detalle
- **Listar todos:** `GET http://localhost:4002/usuarios` (Devuelve lista completa).
- **Detalle por ID:** `GET http://localhost:4002/usuarios/2`

### 5. Modificar Rol o Datos de Usuario (PATCH vs PUT)
- **Método:** `PATCH`
- **URL:** `http://localhost:4002/usuarios/2/rol?rol=ADMIN`
- **Respuesta:** `200 OK` con el objeto actualizado.

> 🗣️ **Pregunta de la Profesora:** *"¿Por qué usan PATCH en lugar de PUT para cambiar el rol o actualizar un campo del usuario?"*  
> **Tu Respuesta:** *"Según el estándar REST (RFC 5789), `PUT` representa un reemplazo completo del recurso (idempotente), requiriendo enviar todos los atributos en el payload. `PATCH` se utiliza para modificaciones parciales de uno o varios campos específicos sin sobreescribir el resto del objeto."*

---

## 🏷️ Fase 2: Categorías (Catálogo Base)

### 6. Crear una Nueva Categoría
- **Método:** `POST`
- **URL:** `http://localhost:4002/categories`
- **Body:**
  ```json
  {
    "nombre": "Accesorios y Calzado"
  }
  ```
- **Respuesta esperada:** `201 Created` con el header `Location: /categories/2` y el JSON de la categoría.

### 7. Listar Categorías con Paginación
- **Método:** `GET`
- **URL:** `http://localhost:4002/categories?page=0&size=10`

---

## 📦 Fase 3: Catálogo de Productos y Reseñas

### 8. Crear un Producto con Stock Real
- **Método:** `POST`
- **URL:** `http://localhost:4002/productos`
- **Body:**
  ```json
  {
    "nombre": "Pantalon Deportivo Azul",
    "descripcion": "Pantalon de acetato colegio San Martin talle 14",
    "precio": 22000.0,
    "talle": "14",
    "stock": 10,
    "estado": "NUEVO",
    "categoryId": 1
  }
  ```
- **Respuesta esperada:** `201 Created` con header `Location: /productos/2`.

> 🗣️ **Pregunta de la Profesora:** *"¿Qué código de estado HTTP debe responder la creación de un producto y qué cabecera debe incluir?"*  
> **Tu Respuesta:** *"Devuelve `201 Created` y la cabecera estándar `Location` con la URI relativa o absoluta del nuevo recurso creado (ej: `/productos/2`)."*

### 9. Búsqueda y Filtros de Productos
- **Listar todos:** `GET http://localhost:4002/productos?page=0&size=10`
- **Filtrar por nombre:** `GET http://localhost:4002/productos/buscar?nombre=Pantalon`
- **Filtrar por rango de precio:** `GET http://localhost:4002/productos/precio?min=10000&max=30000`

### 10. Agregar Comentario a un Producto
- **Método:** `POST`
- **URL:** `http://localhost:4002/comentarios`
- **Body:**
  ```json
  {
    "texto": "Excelente calidad de la tela y muy abrigado.",
    "calificacion": 5,
    "productoId": 2,
    "usuarioId": 2
  }
  ```

### 11. Baja de Producto (Soft Delete)
- **Método:** `DELETE`
- **URL:** `http://localhost:4002/productos/2`
- **Respuesta esperada:** `204 No Content`.

> 🗣️ **Pregunta de la Profesora:** *"¿Qué pasa en la base de datos cuando eliminás un producto? ¿Hacen un DELETE físico con SQL?"*  
> **Tu Respuesta:** *"No, aplicamos un Soft Delete (borrado lógico). Si borráramos el registro físicamente (`DELETE FROM productos`), romperíamos la integridad referencial y el historial de todas las órdenes de compra pasadas. En su lugar, el producto se marca como inactivo o se da de baja lógicamente."*

---

## 🛒 Fase 4: Gestión de Carrito y Expiración

### 12. Crear el Carrito del Usuario
- **Método:** `POST`
- **URL:** `http://localhost:4002/carritos/usuario/2`
- **Respuesta esperada:** `201 Created` con header `Location: /carritos/{id}` y el objeto Carrito vacío.

### 13. Agregar Productos al Carrito
- **Agregar Pantalón (Producto ID: 2, Cantidad: 2):**
  - `POST http://localhost:4002/carritos/1/productos/2?cantidad=2`
  - **Respuesta:** `200 OK` con el carrito conteniendo el ítem y recalculando subtotales.

### 14. Modificar Cantidad en el Carrito (PATCH)
- **Método:** `PATCH`
- **URL:** `http://localhost:4002/carritos/1/productos/2?cantidad=3`
- **Respuesta:** `200 OK` reflejando la nueva cantidad.

### 15. Vencimiento Automático del Carrito (Tarea Asíncrona)
> 🗣️ **Pregunta de la Profesora:** *"¿Qué define que un carrito se venza y cómo lo maneja el sistema sin que un usuario tenga que intervenir?"*  
> **Tu Respuesta:** *"El carrito tiene un campo `fechaCreacion` (o última interacción). Implementamos una tarea programada con Spring (`@EnableScheduling` y `@Scheduled(fixedRate = 60000)` en `CarritoCleanupTask`) que corre periódicamente en segundo plano. Esta tarea busca carritos en estado `ACTIVO` cuya última actividad supere los 30 minutos, liberando los ítems reservados y pasando el estado a `EXPIRADO` o `CANCELADO`."*

---

## 🏷️ Fase 5: Descuentos y Cupones

### 16. Crear un Descuento Promocional
- **Método:** `POST`
- **URL:** `http://localhost:4002/descuentos`
- **Body:**
  ```json
  {
    "porcentaje": 15
  }
  ```
- **Respuesta esperada:** `201 Created` con el ID del descuento (ej: ID: 1).

---

## 💳 Fase 6: Checkout y Generación de Orden de Compra

### 17. Prueba de Error: Compra Sin Stock (Validación de Negocio)
- Crear un carrito o agregar el **Producto ID: 1** (que creamos con stock 0).
- Intentar generar la orden:
  - **Método:** `POST`
  - **URL:** `http://localhost:4002/ordenesDeCompra`
  - **Body:**
    ```json
    {
      "usuarioId": 2,
      "comprobante": "TRANSF-001298",
      "metodoDePago": "TRANSFERENCIA",
      "direccionEnvio": {
        "calle": "Av. Santa Fe",
        "numero": 3000,
        "codigoPostal": "1425",
        "ciudad": "CABA"
      },
      "descuentoId": null
    }
    ```
- **Resultado esperado:** Error `400 Bad Request` indicando que no hay stock disponible.

> 🗣️ **Pregunta de la Profesora:** *"¿Cómo maneja la API los errores de validación o fallas de stock?"*  
> **Tu Respuesta:** *"Manejamos excepciones de negocio con controladores de excepciones (`@ControllerAdvice` o `ResponseStatusException`). Cuando no hay stock disponible, el servicio lanza una excepción específica que se traduce en un código `400 Bad Request` con un mensaje descriptivo en el JSON, en lugar de un error `500 Internal Server Error` no controlado."*

### 18. Compra Exitosa (Con Stock y Descuento)
- Con el carrito conteniendo productos válidos con stock:
- **Método:** `POST`
- **URL:** `http://localhost:4002/ordenesDeCompra`
- **Body:**
  ```json
  {
    "usuarioId": 2,
    "comprobante": "TRANSF-994821",
    "metodoDePago": "TRANSFERENCIA",
    "direccionEnvio": {
      "calle": "Lima",
      "numero": 775,
      "codigoPostal": "1073",
      "ciudad": "CABA"
    },
    "descuentoId": 1
  }
  ```
- **Respuesta esperada:** `201 Created` con la Orden generada, descuento del 15% aplicado sobre el total, y stock descontado en los productos involucrados.

### 19. Consultar Órdenes Realizadas
- **Listar todas:** `GET http://localhost:4002/ordenesDeCompra`
- **Detalle de Orden:** `GET http://localhost:4002/ordenesDeCompra/1`

---

## 🎯 Resumen de Conceptos Clave para la Defensa

| Concepto | Implementación en tu Proyecto |
|---|---|
| **Seguridad de Passwords** | `@JsonIgnore` en la entidad `Usuario` y `BCryptPasswordEncoder`. |
| **PATCH vs PUT** | `PATCH` para actualización parcial (ej. `/usuarios/{id}/rol`, `/carritos/{id}/productos/{id}`); `PUT` reservado para reemplazo integral. |
| **Creación de Recursos** | Retorno de `ResponseEntity.created(URI)` con HTTP `201 Created` y header `Location`. |
| **Borrado Lógico** | `DELETE` cambia el estado del recurso (soft delete) y responde `204 No Content`. |
| **Vencimiento de Carritos** | `@Scheduled` asíncrono cada 60 segundos evaluando inactividad. |
| **Manejo de Stock** | Verificación atómica antes de generar la `OrdenDeCompra`, descontando unidades o abortando con `400`. |
