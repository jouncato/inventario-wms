# Inventario Hexagonal - Backend API

Este proyecto implementa un sistema de inventario utilizando **arquitectura hexagonal (Ports & Adapters)** con Spring Boot, PostgreSQL y buenas prácticas de diseño desacoplado (DDD, SRP, Clean Architecture).

---

## 🧱 Arquitectura

```plaintext
           [ Controladores REST ]
                   ↓
           [ Casos de Uso (UseCase) ]
                   ↓
   [ Dominio (Modelos + Lógica de negocio) ]
                   ↓
        [ Adaptadores de persistencia JPA ]
                   ↓
          [ PostgreSQL via Spring Data ]
```

- **Inbound adapters**: `CategoriaController`, `UserController`, `ProductoController`
- **Application layer**: `CategoriaServiceImpl`, `UserService`, `ProductoServiceImpl`
- **Domain layer**: `*.model`, `*.port.in`, `*.port.out`, `UserDomainService`
- **Outbound adapters**: `*RepositoryAdapter`, `*RepositoryJpa`, `*Entity`, `*Mapper`

---

## ✅ Funcionalidades

### Categorías (`/api/categorias`)
- Crear, actualizar, eliminar, buscar por ID
- Validación de unicidad por nombre

### Productos (`/api/productos`)
- CRUD completo
- Búsqueda por nombre, categoría y precio
- Actualización parcial de stock

### Usuarios (`/api/users`)
- CRUD básico

---

## 🧠 Patrones aplicados

- **Arquitectura Hexagonal**
- **Domain-Driven Design (DDD)**
- **Ports & Adapters**
- **Separación de responsabilidades clara** (modelos, lógica, persistencia)
- **Uso explícito de `Optional`, `ResponseEntity`, `@Service`, `@Repository`, etc.**

---

## 🛠 Tecnologías

- **Java 17+**
- **Spring Boot 3**
- **PostgreSQL**
- **JPA (Hibernate)**
- **Jakarta Persistence (JPA 3.0)**
- **Maven**

---

## 🗃️ Estructura de paquetes

```
com.litethinking.hexagonal
├── adapter.web                    # REST Controllers
├── application.service/usecase    # Casos de uso de aplicación
├── domain.model                   # Modelos de dominio
├── domain.port.in/out             # Puertos hexagonales
├── domain.service                 # Servicios de dominio (negocio)
├── infrastructure.persistence
│   ├── entity                     # Entidades JPA
│   ├── mapper                     # Conversores dominio ↔ entidad
│   └── repository                 # JPA + adaptadores de persistencia
```

---

## ▶️ Cómo ejecutar

```bash
# Requisitos: Java 17, PostgreSQL corriendo

./mvnw spring-boot:run
```

---

## 📌 Notas

- Se recomienda agregar DTOs para entrada/salida en controllers si se desea proteger el dominio.
- Preparado para pruebas unitarias con puertos desacoplados.
- Plantilla ideal para escalar con microservicios o eventos de dominio.

---

## 🚧 Próximos pasos sugeridos

- Agregar DTOs y validaciones (`@Valid`, `@NotBlank`)
- Documentar con Swagger/OpenAPI
- Soporte para paginación y ordenamiento
- Agregar tests de unidad y de integración (MockMvc, Testcontainers)
