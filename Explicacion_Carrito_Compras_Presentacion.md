# Guía Completa de Presentación: Carrito de Compras

Esta es la guía completa y detallada para que puedas defender y explicar con total seguridad el flujo del **Carrito de Compras** ante tu profesora.

Está organizada en dos partes:
1. **El Recorrido Conceptual y de Arquitectura (La "Historia" que le contas a la profesora).**
2. **Explicación Línea por Línea del Código**, dividida por capas (Entidades, Repositorios, Controlador y Servicio).

---

# 1. El Recorrido Conceptual (Visión General para la Presentación)

Cuando la profesora te pregunte *"¿Cómo funciona el carrito de compras en tu sistema?"*, tu respuesta estructurada es:

1. **Arquitectura en Capas (Layered Architecture):**
   * **Entidad / Modelo:** Define las tablas en la base de datos relacional mediante JPA/Hibernate (`Carrito` y `ItemCarrito`).
   * **Repositorio (`Repository`):** Interfaces que extienden `JpaRepository` para interactuar con la base de datos sin escribir SQL manual (`CarritoRepository` e `ItemCarritoRepository`).
   * **Servicio (`Service`):** Contiene la lógica de negocio y las validaciones (verificación de existencia, validación de stock disponible, actualización incremental de cantidades y transaccionalidad `@Transactional`).
   * **Controlador (`Controller`):** Expone los endpoints REST HTTP (`GET`, `POST`, `PUT`, `DELETE`), mapea URLs, parámetros y devuelve respuestas estandarizadas con códigos HTTP (`200 OK`, `201 Created`, etc.).

2. **El Ciclo de Vida del Carrito en el Negocio:**
   * **Creación:** Cada usuario tiene **un único carrito** (relación `1 a 1`). Se crea vía `POST /carritos/usuario/{usuarioId}`. Si ya tiene uno, el sistema lanza una excepción de regla de negocio.
   * **Agregar Ítems:** Vía `POST /carritos/{carritoId}/productos/{productoId}?cantidad=X`. Valida que el producto exista y que haya stock disponible en inventario. Si el producto ya estaba en el carrito, suma la cantidad (no duplica filas); si no estaba, crea un nuevo `ItemCarrito`.
   * **Modificar Cantidad / Eliminar Ítem:** `PUT` para ajustar cantidad o `DELETE` para quitar un producto puntual.
   * **Cierre / Checkout (Paso a Orden de Compra):** Al generar la `OrdenDeCompra`, el sistema toma los ítems del carrito, descuenta definitivamente el stock de los productos, calcula el total (con o sin descuento aplicado) y finalmente **vacía el carrito**, dejándolo listo para futuras compras.

---

# 2. Explicación Línea por Línea de Todo el Código

---

### A. Capa de Datos: Entidades

#### `Carrito.java`
```java
7: @Data
```
> **Lombok:** Genera automáticamente en tiempo de compilación los getters, setters, `equals`, `hashCode` y `toString`, evitando código repetitivo (*boilerplate*).

```java
8: @Entity
```
> **JPA:** Le indica a Hibernate que esta clase se mapea como una tabla en la base de datos relacional.

```java
9: public class Carrito {
10:     public Carrito() {
11:     }
```
> Constructor vacío obligatorio que JPA necesita para instanciar la entidad al recuperar datos.

```java
13:     @Id
14:     @GeneratedValue(strategy = GenerationType.IDENTITY)
15:     private long id;
```
> Clave primaria (`@Id`) autoincremental (`GenerationType.IDENTITY`) delegada al motor de base de datos.

```java
17:     @OneToOne
18:     @JoinColumn(name = "usuario_id", nullable = false)
19:     private Usuario usuario;
```
> **Relación 1 a 1:** Cada carrito pertenece estrictamente a un único usuario. En la tabla se crea una clave foránea (FK) llamada `usuario_id` que no puede ser nula (`nullable = false`).

```java
21:     @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
22:     private List<ItemCarrito> items;
```
> **Relación 1 a N:** Un carrito contiene múltiples ítems.
> * `mappedBy = "carrito"`: La relación bidireccional está administrada por el atributo `carrito` en `ItemCarrito`.
> * `cascade = CascadeType.ALL`: Si se guarda o actualiza el carrito, sus ítems se persisten automáticamente en cascada.
> * `orphanRemoval = true`: Si un ítem se elimina de esta lista, JPA lo borra automáticamente de la base de datos.

---

#### `ItemCarrito.java`
Representa cada línea individual dentro de un carrito (producto + cantidad elegida + precio unitario).

```java
9: @Builder
10: @Data
11: @NoArgsConstructor
12: @AllArgsConstructor
13: @Entity
14: @Table(name = "items_carrito")
```
> * `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`: Patrón builder y constructores con/sin argumentos vía Lombok.
> * `@Table(name = "items_carrito")`: Personaliza el nombre de la tabla en la base de datos.

```java
17:     @Id
18:     @GeneratedValue(strategy = GenerationType.IDENTITY)
19:     private Long id;
```
> Identificador único (PK) del ítem en la base de datos.

```java
21:     @ManyToOne
22:     @JoinColumn(name = "carrito_id", nullable = false)
23:     private Carrito carrito;
```
> **Relación Muchos a Uno:** Muchos ítems pertenecen al mismo carrito. Clave foránea `carrito_id`.

```java
25:     @ManyToOne
26:     @JoinColumn(name = "producto_id", nullable = false)
27:     private Producto producto;
```
> **Relación Muchos a Uno:** Referencia al producto que se está comprando (`producto_id`).

```java
29:     @Column(nullable = false)
30:     private int cantidad;
32:     @Column(nullable = false)
33:     private double precioUnitario;
```
> Atributos obligatorios: cuántas unidades lleva y a qué precio unitario se cargó en ese momento.

---

### B. Capa de Persistencia: Repositorios

#### `CarritoRepository.java`
```java
7: public interface CarritoRepository extends JpaRepository<Carrito, Long> {
8:     Optional<Carrito> findByUsuarioId(Long usuarioId);
9: }
```
> * Extiende de `JpaRepository<Carrito, Long>`, lo que ya otorga métodos CRUD (`save`, `findById`, `findAll`, `delete`).
> * `findByUsuarioId(...)`: **Query Method derivado** de Spring Data JPA. Por convención de nombres, Spring genera la consulta SQL: `SELECT * FROM carrito WHERE usuario_id = ?`. Devuelve un `Optional` para manejar elegantemente si el carrito no existe sin arrojar `NullPointerException`.

#### `ItemCarritoRepository.java`
```java
8: public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
9:     Optional<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
10:    List<ItemCarrito> findByCarritoId(Long carritoId);
11: }
```
> * `findByCarritoIdAndProductoId(...)`: Busca si un producto determinado ya está dentro de un carrito específico (evita duplicados).
> * `findByCarritoId(...)`: Trae todos los ítems asociados a un carrito.

---

### C. Capa de Controladores (API REST): `CarritoController.java`

```java
12: @RestController
13: @RequestMapping("carritos")
14: public class CarritoController {
15:     @Autowired
16:     private CarritoService carritoService;
```
> * `@RestController`: Combina `@Controller` y `@ResponseBody`. Convierte automáticamente los objetos Java devueltos en formato JSON.
> * `@RequestMapping("carritos")`: Prefijo de URL para todos los endpoints de este controlador (`/carritos`).
> * `@Autowired`: Inyección de dependencias de Spring. Inyecta la implementación del servicio (`CarritoServiceImpl`).

```java
18:     @GetMapping
19:     public List<Carrito> getCarritos() {
20:         return carritoService.getCarritos();
21:     }
```
> **GET `/carritos`**: Retorna la lista con todos los carritos del sistema (útil para auditoría/administrador).

```java
23:     @GetMapping("/{carritoId}")
24:     public Optional<Carrito> getCarritoById(@PathVariable Long carritoId) {
25:         return carritoService.getCarritoById(carritoId);
26:     }
```
> **GET `/carritos/{carritoId}`**: Obtiene el carrito por su ID numérico. `@PathVariable` captura el valor desde la ruta de la URL.

```java
28:     @GetMapping("/usuario/{usuarioId}")
29:     public Optional<Carrito> getCarritoByUsuarioId(@PathVariable Long usuarioId) {
30:         return carritoService.getCarritoByUsuarioId(usuarioId);
31:     }
```
> **GET `/carritos/usuario/{usuarioId}`**: Permite a la aplicación frontend consultar directamente el carrito del usuario autenticado.

```java
34:     @PostMapping("/usuario/{usuarioId}")
35:     public ResponseEntity<Object> createCarrito(@PathVariable Long usuarioId) {
36:         Carrito resultado = carritoService.createCarrito(usuarioId);
37:         return ResponseEntity
38:                 .created(URI.create("/carritos/" + resultado.getId()))
39:                 .body(resultado);
40:     }
```
> **POST `/carritos/usuario/{usuarioId}`**: Crea el carrito para el usuario.
> * Devuelve un código HTTP `201 Created` con el header `Location` apuntando al recurso creado (`/carritos/{id}`) y el carrito en el cuerpo JSON (`body`). Cumple con las buenas prácticas REST.

```java
43:     @PostMapping("/{carritoId}/productos/{productoId}")
44:     public ResponseEntity<Carrito> addProductoToCarrito(
45:             @PathVariable Long carritoId,
46:             @PathVariable Long productoId,
47:             @RequestParam(defaultValue = "1") int cantidad) {
48:         Carrito carritoActualizado = carritoService.addProductoToCarrito(carritoId, productoId, cantidad);
49:         return ResponseEntity.ok(carritoActualizado);
50:     }
```
> **POST `/carritos/{carritoId}/productos/{productoId}?cantidad=X`**: Agrega un producto al carrito.
> * `@RequestParam(defaultValue = "1")`: Si el cliente no especifica el parámetro `cantidad` en la URL, asume por defecto `1`.
> * Devuelve un código HTTP `200 OK` con el carrito actualizado y sus ítems.

```java
53:     @PutMapping("/{carritoId}/productos/{productoId}")
54:     public ResponseEntity<Carrito> updateCantidadProducto(
55:             @PathVariable Long carritoId,
56:             @PathVariable Long productoId,
57:             @RequestParam int cantidad) {
58:         Carrito carritoActualizado = carritoService.updateCantidadProducto(carritoId, productoId, cantidad);
59:         return ResponseEntity.ok(carritoActualizado);
60:     }
```
> **PUT `/carritos/{carritoId}/productos/{productoId}?cantidad=X`**: Modifica o sobreescribe la cantidad de un ítem existente en el carrito.

```java
63:     @DeleteMapping("/{carritoId}/productos/{productoId}")
64:     public ResponseEntity<Carrito> removeProductoFromCarrito(@PathVariable Long carritoId, @PathVariable Long productoId) {
65:         Carrito carritoActualizado = carritoService.removeProductoFromCarrito(carritoId, productoId);
66:         return ResponseEntity.ok(carritoActualizado);
67:     }
```
> **DELETE `/carritos/{carritoId}/productos/{productoId}`**: Quita completamente ese producto del carrito y retorna el estado final del carrito.

---

### D. Capa de Negocio (Lógica de Dominio): `CarritoServiceImpl.java`

Esta es la clase más importante para la profesora porque concentra las validaciones de negocio.

```java
21: @Service
22: public class CarritoServiceImpl implements CarritoService {
23:     @Autowired
24:     private CarritoRepository carritoRepository;
26:     @Autowired
27:     private ItemCarritoRepository itemCarritoRepository;
29:     @Autowired
30:     private UsuarioService usuarioService;
32:     @Autowired
33:     private ProductoService productoService;
```
> `@Service`: Marca la clase como componente de servicio de Spring dentro del contenedor IoC (Inversión de Control).
> Inyecta los repositorios y servicios auxiliares necesarios para validar usuarios y productos.

```java
35:     public List<Carrito> getCarritos() {
36:         return carritoRepository.findAll();
37:     }
39:     public Optional<Carrito> getCarritoById(Long carritoId) {
40:         return carritoRepository.findById(carritoId);
41:     }
43:     public Optional<Carrito> getCarritoByUsuarioId(Long usuarioId) {
44:         return carritoRepository.findByUsuarioId(usuarioId);
45:     }
```
> Métodos de consulta de lectura simples delegados a los repositorios.

---

#### Creación del Carrito:
```java
47:     @Transactional(rollbackFor = Throwable.class)
48:     public Carrito createCarrito(Long usuarioId) {
49:         if (carritoRepository.findByUsuarioId(usuarioId).isPresent())
50:             throw new ReglaDeNegocioException("El usuario ya tiene un carrito creado");
```
> * `@Transactional(rollbackFor = Throwable.class)`: Asegura atomicidad. Si ocurre cualquier error o excepción, revierte (rollback) los cambios en la base de datos.
> * **Regla de Negocio:** Valida que el usuario no tenga ya un carrito. Si ya existe, interrumpe el flujo arrojando una excepción personalizada de negocio.

```java
52:         Usuario usuario = usuarioService.getUsuarioById(usuarioId)
53:                 .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId));
```
> Verifica si el usuario existe. Si el `Optional` está vacío, arroja un `RecursoNoEncontradoException` (que se traducirá en un `404 Not Found`).

```java
55:         Carrito carrito = new Carrito();
56:         carrito.setUsuario(usuario);
57:         return carritoRepository.save(carrito);
58:     }
```
> Instancia la entidad, asocia al usuario y la persiste en la base de datos con `.save()`.

---

#### Agregar Producto al Carrito (Método Clave):
```java
60:     @Transactional(rollbackFor = Throwable.class)
61:     public Carrito addProductoToCarrito(Long carritoId, Long productoId, int cantidad) {
62:         Carrito carrito = carritoRepository.findById(carritoId)
63:                 .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));
65:         Producto producto = productoService.getProductoById(productoId)
66:                 .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));
```
> 1. Busca el carrito por ID; si no existe, lanza excepción 404.
> 2. Busca el producto por ID; si no existe, lanza excepción 404.

```java
68:         if (producto.getStock() < cantidad) {
69:             throw new StockInsuficienteException("No hay stock suficiente para el producto: " + producto.getNombre());
70:         }
```
> **Validación de Stock Inicial:** Comprueba que la tienda tenga al menos las unidades solicitadas.

```java
72:         Optional<ItemCarrito> itemExistente =
73:                 itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId);
```
> Consulta si el producto ya estaba previamente agregado en este carrito.

```java
75:         if (itemExistente.isPresent()) {
76:             ItemCarrito item = itemExistente.get();
77:             if (producto.getStock() < item.getCantidad() + cantidad) {
78:                 throw new StockInsuficienteException("No hay stock suficiente para el producto: " + producto.getNombre());
79:             }
80:             item.setCantidad(item.getCantidad() + cantidad);
81:             itemCarritoRepository.save(item);
```
> **Caso 1 (El ítem ya existía):**
> * Calcula si la suma de lo que ya tenía (`item.getCantidad()`) más lo nuevo (`cantidad`) supera el stock disponible.
> * Si supera el stock, frena con `StockInsuficienteException`.
> * Si hay stock, suma las cantidades y actualiza la fila existente con `.save(item)`.

```java
82:         } else {
83:             if (carrito.getItems() == null)
84:                 carrito.setItems(new ArrayList<>());
85: 
86:             ItemCarrito nuevoItem = new ItemCarrito();
87:             nuevoItem.setCarrito(carrito);
88:             nuevoItem.setProducto(producto);
89:             nuevoItem.setCantidad(cantidad);
90:             nuevoItem.setPrecioUnitario(producto.getPrecio());
91:             carrito.getItems().add(nuevoItem);
92:         }
```
> **Caso 2 (Es un producto nuevo en el carrito):**
> * Inicializa la lista de ítems si estuviese nula (evita `NullPointerException`).
> * Crea la entidad `ItemCarrito`, vinculando el carrito, el producto, la cantidad y congelando el `precioUnitario` actual del producto.
> * Lo añade a la colección `carrito.getItems()`.

```java
94:         return carritoRepository.save(carrito);
95:     }
```
> Guarda el carrito (y por efecto del `CascadeType.ALL`, guarda también el nuevo ítem) y devuelve la entidad completa actualizada.

---

#### Actualizar Cantidad de un Producto:
```java
97:     @Transactional(rollbackFor = Throwable.class)
98:     public Carrito updateCantidadProducto(Long carritoId, Long productoId, int cantidad) {
99:         ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId)
100:                 .orElseThrow(() -> new RecursoNoEncontradoException("El producto no se encuentra en el carrito"));
102:         item.setCantidad(cantidad);
103:         itemCarritoRepository.save(item);
105:         return carritoRepository.findById(carritoId)
106:                 .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));
107:     }
```
> * Busca el ítem en la base de datos por la combinación `(carritoId, productoId)`.
> * Actualiza directamente la cantidad al valor recibido.
> * Guarda el ítem y recarga el carrito para devolver su estado final.

---

#### Eliminar un Producto del Carrito:
```java
109:     @Transactional(rollbackFor = Throwable.class)
110:     public Carrito removeProductoFromCarrito(Long carritoId, Long productoId) {
111:         Carrito carrito = carritoRepository.findById(carritoId)
112:                 .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));
114:         ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId)
115:                 .orElseThrow(() -> new RecursoNoEncontradoException("El producto no se encuentra en el carrito"));
117:         carrito.getItems().remove(item);
118:         itemCarritoRepository.delete(item);
119:         return carritoRepository.save(carrito);
120:     }
```
> * Obtiene el carrito y el ítem a borrar.
> * Lo desvincula de la lista en memoria del carrito: `carrito.getItems().remove(item)`.
> * Lo borra físicamente de la tabla: `itemCarritoRepository.delete(item)`.
> * Retorna el carrito actualizado.

---

#### Vaciar Carrito:
```java
122:     @Transactional(rollbackFor = Throwable.class)
123:     public void vaciarCarrito(Long carritoId) {
124:         Carrito carrito = carritoRepository.findById(carritoId)
125:                 .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado con id: " + carritoId));
126:         
127:         itemCarritoRepository.deleteAll(carrito.getItems());
128:         carrito.getItems().clear();
129:         carritoRepository.save(carrito);
130:     }
```
> Borra todos los ítems asociados al carrito y limpia la lista (`.clear()`). Es utilizado luego al finalizar una orden de compra.

---

### E. Conexión con el Checkout: `OrdenDeCompraServiceImpl.java`

Si la profesora te pregunta: *"¿Y qué pasa con el carrito cuando el cliente compra?"*:

1. **Recupera el carrito del usuario:**
   ```java
   Carrito carrito = carritoRepository.findByUsuarioId(usuario.getId())
           .orElseThrow(() -> new ReglaDeNegocioException("El usuario no tiene un carrito activo"));
   ```
2. **Valida que no esté vacío:**
   ```java
   if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
       throw new ReglaDeNegocioException("El carrito está vacío");
   }
   ```
3. **Itera los ítems para descontar stock real y calcular el total:**
   ```java
   for (ItemCarrito item : carrito.getItems()) {
       Producto producto = item.getProducto();
       // Valida stock de nuevo
       producto.setStock(producto.getStock() - item.getCantidad());
       productoRepository.save(producto);
       // Crea el DetalleOrden histórico...
   }
   ```
4. **Vacía el carrito tras guardar la orden:**
   ```java
   itemCarritoRepository.deleteAll(carrito.getItems());
   carrito.getItems().clear();
   carritoRepository.save(carrito);
   ```

---

### 3 Tips para deslumbrar a la profesora durante la defensa:
1. **Mencioná la anotación `@Transactional`:** Explicá que garantiza que si a mitad del proceso se corta la conexión o falta stock, no quede el carrito inconsistente en la base de datos (propiedades ACID).
2. **Explicá por qué existe `ItemCarrito`:** Explicá que `ItemCarrito` es una tabla intermedia que descompone la relación muchos a muchos entre `Carrito` y `Producto`, guardando datos propios de la relación como la `cantidad` y el `precioUnitario`.
3. **Mapeo de excepciones:** Mostrá que las excepciones como `StockInsuficienteException` y `RecursoNoEncontradoException` no rompen el servidor con un error `500`, sino que son capturadas por el `GlobalExceptionHandler` para devolver respuestas HTTP claras (`400 Bad Request` o `404 Not Found`).
