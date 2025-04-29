# 📦 Suite de Pruebas Automatizadas - Inventario Hexagonal

Este documento describe la estructura, cobertura y ejecución de la suite de pruebas automatizadas para el backend del sistema de inventario basado en arquitectura hexagonal.

---

## ✅ Cobertura de Pruebas

| Tipo               | Clase                               | Archivo                                 |
|--------------------|--------------------------------------|------------------------------------------|
| **Unidad**         | CategoriaServiceImpl                | `CategoriaServiceImplTest.java`         |
|                    | ProductoServiceImpl                 | `ProductoServiceImplTest.java`          |
|                    | UserService                         | `UserServiceTest.java`                  |
| **REST (MockMvc)** | CategoriaController                 | `CategoriaControllerTest.java`          |
|                    | ProductoController                  | `ProductoControllerTest.java`           |
|                    | UserController                      | `UserControllerTest.java`               |
| **Integración**    | ProductoController + JPA + H2       | `ProductoIntegrationTest.java`          |
|                    | CategoriaController + JPA + H2      | `CategoriaIntegrationTest.java`         |
|                    | UserController + JPA + H2           | `UserIntegrationTest.java`              |

---

## 🛠 Requisitos

- Java 17+
- Spring Boot 3+
- Gradle 7+
- Base de datos en memoria (H2 para integración)
- Mockito para mocking

---

## 🚀 Cómo ejecutar las pruebas

Desde consola:

```bash
./gradlew test
```

Para ver el reporte HTML de pruebas:

```bash
open build/reports/tests/test/index.html
```

---

## 🧪 Estructura

```
src
├── main
│   └── java
│       └── com.litethinking.hexagonal
├── test
│   └── java
│       ├── ...ControllerTest.java       # REST con MockMvc
│       ├── ...ServiceImplTest.java      # Unitarios
│       └── integration/
│           └── ...IntegrationTest.java  # SpringBootTest con H2
```

---

## 🧰 Configuración para H2

Asegúrate de tener este archivo en `src/test/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    database-platform: org.hibernate.dialect.H2Dialect
  h2:
    console:
      enabled: true
```

---

## 🔄 Extensión recomendada

- Cobertura con JaCoCo
- Tests parametrizados con `@ParameterizedTest`
- Pruebas de contrato con Spring Cloud Contract
- Testcontainers para usar PostgreSQL real en CI

---

## 💬 Soporte

Para dudas, contacta con ................