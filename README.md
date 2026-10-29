# AutoAndes Backend REST API

Sistema backend empresarial para el taller mecánico **AutoAndes**, desarrollado con **Java 21** y **Spring Boot 3.4.3**, con persistencia en **Oracle Database**, migraciones automatizadas mediante **Flyway**, seguridad con **Spring Security** y **JWT (JSON Web Tokens)**, arquitectura desacoplada con **DTOs**, mappers y manejo centralizado de excepciones.

---

## 1. Tecnologías y Versiones

- **Java**: 21 (Eclipse Temurin / OpenJDK 21 LTS)
- **Spring Boot**: 3.4.3
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-security`
  - `spring-boot-starter-validation`
- **Base de Datos**: Oracle Database (XE / EE / Cloud)
- **Driver JDBC**: `ojdbc11`
- **Migración de BD**: Flyway (`flyway-core` + `flyway-database-oracle`)
- **Seguridad**: JJWT `0.12.6` (HMAC-SHA256) + BCrypt Password Encoder
- **Documentación API**: SpringDoc OpenAPI UI `2.8.5` (Swagger 3)
- **Lombok**: Simplificación de boilerplate con anotaciones (`@Data`, `@Slf4j`, etc.)

---

## 2. Estructura de Paquetes

```
pe.edu.upeu.autoandes
├── config/
│   ├── CorsConfig.java
│   ├── OpenApiConfig.java
│   ├── SecurityConfig.java
│   └── UserInitializerRunner.java
├── controller/
│   ├── AuthController.java
│   ├── ClienteController.java
│   ├── HealthController.java
│   ├── OrdenServicioController.java
│   ├── ReporteController.java
│   ├── ServicioController.java
│   └── VehiculoController.java
├── dto/
│   ├── request/
│   │   ├── AnularOrdenRequestDTO.java
│   │   ├── CambiarEstadoServicioDTO.java
│   │   ├── DetalleOrdenRequestDTO.java
│   │   ├── LoginRequestDTO.java
│   │   ├── OrdenServicioRequestDTO.java
│   │   ├── ServicioRequestDTO.java
│   │   └── VehiculoRequestDTO.java
│   └── response/
│       ├── ClienteResponseDTO.java
│       ├── DetalleOrdenResponseDTO.java
│       ├── IngresoServicioReporteDTO.java
│       ├── LoginResponseDTO.java
│       ├── OrdenServicioResponseDTO.java
│       ├── ServicioResponseDTO.java
│       ├── UsuarioResponseDTO.java
│       └── VehiculoResponseDTO.java
├── entity/
│   ├── Cliente.java
│   ├── DetalleOrden.java
│   ├── OrdenServicio.java
│   ├── Rol.java
│   ├── Servicio.java
│   ├── Usuario.java
│   └── Vehiculo.java
├── enums/
│   ├── EstadoOrden.java
│   └── Permiso.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── RecursoNoEncontradoException.java
│   ├── ReglaNegocioException.java
│   └── dto/
│       └── ErrorResponseDTO.java
├── mapper/
│   ├── ClienteMapper.java
│   ├── OrdenServicioMapper.java
│   ├── ServicioMapper.java
│   ├── UsuarioMapper.java
│   └── VehiculoMapper.java
├── repository/
│   ├── ClienteRepository.java
│   ├── DetalleOrdenRepository.java
│   ├── OrdenServicioRepository.java
│   ├── RolRepository.java
│   ├── ServicioRepository.java
│   ├── UsuarioRepository.java
│   └── VehiculoRepository.java
└── service/
    ├── impl/
    │   ├── AuthServiceImpl.java
    │   ├── ClienteServiceImpl.java
    │   ├── OrdenServicioServiceImpl.java
    │   ├── ReporteServiceImpl.java
    │   ├── ServicioServiceImpl.java
    │   └── VehiculoServiceImpl.java
    └── service/
        ├── AuthService.java
        ├── ClienteService.java
        ├── OrdenServicioService.java
        ├── ReporteService.java
        ├── ServicioService.java
        └── VehiculoService.java
```

---

## 3. Usuarios de Prueba (Semilla)

| Usuario | Contraseña | Rol | Estado | Permisos Asignados |
| :--- | :--- | :--- | :--- | :--- |
| `admin` | `Auto2026*` | `ADMIN` | Activo | Todos los permisos del sistema |
| `asesor` | `Auto2026*` | `ASESOR` | Activo | `SERVICIO_LEER`, `VEHICULO_LEER`, `VEHICULO_ESCRIBIR`, `ORDEN_LEER`, `ORDEN_REGISTRAR` |
| `jefe` | `Auto2026*` | `JEFE_TALLER` | Activo | `SERVICIO_LEER`, `SERVICIO_ESCRIBIR`, `VEHICULO_LEER`, `ORDEN_LEER`, `ORDEN_ANULAR`, `REPORTE_VER` |
| `inactivo` | `Auto2026*` | `ASESOR` | Inactivo | Rechazado con HTTP 401 |

---

## 4. Matriz de Autorización por Permisos (`hasAuthority`)

| Permiso | Acción Permitida | ADMIN | ASESOR | JEFE_TALLER |
| :--- | :--- | :---: | :---: | :---: |
| `SERVICIO_LEER` | Consultar servicios | Sí | Sí | Sí |
| `SERVICIO_ESCRIBIR` | Crear, editar, dar de baja servicios | Sí | No | Sí |
| `VEHICULO_LEER` | Consultar vehículos y clientes | Sí | Sí | Sí |
| `VEHICULO_ESCRIBIR` | Registrar, editar y eliminar vehículos | Sí | Sí | No |
| `ORDEN_LEER` | Consultar órdenes y detalles | Sí | Sí | Sí |
| `ORDEN_REGISTRAR` | Registrar nueva orden de servicio | Sí | Sí | No |
| `ORDEN_ANULAR` | Anular órdenes de servicio | Sí | No | Sí |
| `REPORTE_VER` | Ver reportes de ingresos | Sí | No | Sí |

---

## 5. Contrato de la API REST (`/api/v1`)

| Método | Endpoint | Permiso Requerido | Códigos HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Público | `200`, `400`, `401` |
| `GET` | `/api/v1/auth/me` | Autenticado | `200`, `401` |
| `GET` | `/api/v1/clientes` | `VEHICULO_LEER` | `200`, `401`, `403` |
| `GET` | `/api/v1/clientes/{id}` | `VEHICULO_LEER` | `200`, `401`, `403`, `404` |
| `GET` | `/api/v1/servicios` | `SERVICIO_LEER` | `200`, `401`, `403` |
| `POST` | `/api/v1/servicios` | `SERVICIO_ESCRIBIR` | `201`, `400`, `401`, `403`, `409` |
| `GET` | `/api/v1/servicios/{id}` | `SERVICIO_LEER` | `200`, `401`, `403`, `404` |
| `PUT` | `/api/v1/servicios/{id}` | `SERVICIO_ESCRIBIR` | `200`, `400`, `401`, `403`, `404`, `409` |
| `DELETE` | `/api/v1/servicios/{id}` | `SERVICIO_ESCRIBIR` | `204`, `401`, `403`, `404`, `409` |
| `PATCH` | `/api/v1/servicios/{id}/estado` | `SERVICIO_ESCRIBIR` | `200`, `401`, `403`, `404` |
| `GET` | `/api/v1/vehiculos?clienteId` | `VEHICULO_LEER` | `200`, `401`, `403`, `404` |
| `POST` | `/api/v1/vehiculos` | `VEHICULO_ESCRIBIR` | `201`, `400`, `401`, `403`, `404`, `409` |
| `GET` | `/api/v1/vehiculos/{id}` | `VEHICULO_LEER` | `200`, `401`, `403`, `404` |
| `PUT` | `/api/v1/vehiculos/{id}` | `VEHICULO_ESCRIBIR` | `200`, `400`, `401`, `403`, `404`, `409` |
| `DELETE` | `/api/v1/vehiculos/{id}` | `VEHICULO_ESCRIBIR` | `204`, `401`, `403`, `404`, `409` |
| `POST` | `/api/v1/ordenes` | `ORDEN_REGISTRAR` | `201`, `400`, `401`, `403`, `404`, `409` |
| `GET` | `/api/v1/ordenes?placa&estado&desde&hasta` | `ORDEN_LEER` | `200`, `401`, `403` |
| `GET` | `/api/v1/ordenes/{id}` | `ORDEN_LEER` | `200`, `401`, `403`, `404` |
| `PATCH` | `/api/v1/ordenes/{id}/anular` | `ORDEN_ANULAR` | `200`, `400`, `403`, `404`, `409` |
| `GET` | `/api/v1/reportes/ingresos-por-servicio?desde&hasta` | `REPORTE_VER` | `200`, `401`, `403` |
| `GET` | `/api/v1/health` | Público | `200` |

---

## 6. Reglas de Negocio Implementadas

- **RN-01 (Servicios)**: Código único con formato `SRV-000` (409 en colisión). Si tiene órdenes de servicio asociadas no se elimina físicamente (DELETE retorna 409); se provee `PATCH /servicios/{id}/estado` para alternar su disponibilidad.
- **RN-02 (Vehículos)**: Placa única con formato `ABC-123` (409). Validación estricta de existencia del cliente (404 si no existe). No se permite eliminar vehículos con órdenes asociadas (409).
- **RN-03 (Órdenes de Servicio)**: Cabecera con entre 1 y 10 líneas de detalle; no se admiten servicios duplicados dentro de la misma orden; cantidades entre 1 y 5 unidades (400 si se vulnera). Todos los servicios deben estar en estado activo (`activo = 1`), de lo contrario se rechaza con 409 indicando el servicio inactivo.
- **RN-04 (Cálculo y Transaccionalidad)**: El precio unitario se toma obligatoriamente de la base de datos (se descarta cualquier valor enviado por el cliente). Subtotal = precio BD * cantidad; Total = sumatoria de subtotales. El kilometraje no puede ser inferior al de la última orden `REGISTRADA` del mismo vehículo (409 indicando el kilometraje anterior). Generación correlativa automática única `OS-000001`. Toda la operación se ejecuta bajo `@Transactional(rollbackFor = Exception.class)`, garantizando que ningún registro quede a medias ante fallos.
- **RN-05 (Anulación)**: Solo se permite anular órdenes en estado `REGISTRADA` y emitidas en el mismo día. Motivo obligatorio de 10 a 200 caracteres (400 por validación / 409 por estado o fecha).
- **Reporte Agregado**: Consulta JPQL nativa con agregación (`GROUP BY s.codigo, s.nombre`) proyectando a `IngresoServicioReporteDTO`, ordenada por importe total descendente y excluyendo órdenes con estado `ANULADA`. Prohibido el uso de `findAll()` con filtrado en bucles de memoria.

---

## 7. Formato Común de Respuestas de Error

Todas las respuestas de error (`400`, `401`, `403`, `404`, `409`, `500`) cumplen el formato estándar exigido:

```json
{
  "timestamp": "2026-10-29T09:14:22",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Datos inválidos",
  "ruta": "/api/v1/vehiculos",
  "campos": {
    "placa": "La placa debe tener el formato ABC-123"
  }
}
```

---

## 8. Ejecución y Acceso a Swagger UI

### Compilar y Ejecutar Pruebas:
```bash
./mvnw test
```

### Iniciar el Servidor:
```bash
./mvnw spring-boot:run
```

### Documentación Interactiva Swagger UI:
Una vez levantado el backend, abrir en el navegador:
`http://localhost:8080/swagger-ui.html` o `http://localhost:8080/swagger-ui/index.html`
