# ms-datos — Microservicio de Datos Organizacionales

Centraliza y almacena métricas e indicadores operacionales provenientes de distintas fuentes de negocio (ventas, inventario, finanzas, e-commerce) para las sucursales de Grupo Cordillera.

---

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Build | Maven 3.9.6 |
| ORM | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8.0 |
| HTTP Client | Spring WebFlux (WebClient) |

---

## Puerto

| Entorno | Puerto |
|---|---|
| Local | `8081` |
| Docker | `8081` (mapeado `8081:8081`) |

---

## Base de datos

- **Motor:** MySQL 8.0
- **Nombre:** `baseorganizacional`
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`

### Entidad principal: `DatoOrganizacional`

| Campo | Tipo | Nulable | Descripción |
|---|---|---|---|
| `id` | Long | No | PK auto-generada |
| `fuente` | String | No | Origen del dato: `VENTAS`, `INVENTARIO`, `FINANZAS`, `ECOMMERCE` |
| `indicador` | String | No | Nombre del indicador (p. ej. `ingresos_mes`) |
| `valor` | Double | No | Valor numérico del indicador |
| `fecha` | LocalDate | No | Fecha del registro |
| `sucursal` | String | Sí | Identificador de sucursal |

---

## Variables de entorno

| Variable | Valor por defecto (local) | Valor Docker |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/baseorganizacional` | `jdbc:mysql://db-datos:3306/baseorganizacional` |
| `SPRING_DATASOURCE_USERNAME` | `root` | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `211822` | `root` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | `com.mysql.cj.jdbc.Driver` | `com.mysql.cj.jdbc.Driver` |

---

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo en `localhost:3306` con la base de datos `baseorganizacional` creada

```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS baseorganizacional;"

# Compilar y ejecutar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8081`.

---

## Ejecución con Docker

```bash
# Desde el directorio msdatos/
docker compose up --build
```

Levanta dos contenedores:
- `db-datos` — MySQL 8.0 con volumen persistente `datos-data`
- `ms-datos` — aplicación Spring Boot, espera a que la BD esté saludable

Para detener y eliminar los contenedores:

```bash
docker compose down
```

Para eliminar también los volúmenes:

```bash
docker compose down -v
```

---

## Endpoints

Base path: `/api/datos`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/datos` | Lista todos los registros de datos organizacionales |
| `GET` | `/api/datos/fuente/{fuente}` | Filtra registros por fuente (`VENTAS`, `INVENTARIO`, `FINANZAS`, `ECOMMERCE`) |
| `GET` | `/api/datos/sucursal/{sucursal}` | Filtra registros por sucursal |
| `POST` | `/api/datos` | Crea un nuevo registro de dato organizacional |
| `DELETE` | `/api/datos/{id}` | Elimina un registro por ID |

### Ejemplo de body para `POST /api/datos`

```json
{
  "fuente": "VENTAS",
  "indicador": "ingresos_mes",
  "valor": 15000000.0,
  "fecha": "2025-05-01",
  "sucursal": "Santiago Centro"
}
```

---

## Patrones implementados

| Patrón | Descripción |
|---|---|
| **Repository Pattern** | `DatoOrganizacionalRepository` extiende `JpaRepository`; encapsula el acceso a datos |
| **Service Layer** | Interfaz `DatoOrganizacionalService` + implementación `DatoOrganizacionalServiceImpl`; desacopla lógica de negocio del controlador |
| **Global Exception Handler** | `@RestControllerAdvice` en `GlobalExceptionHandler`; respuestas de error estandarizadas |
| **Dependency Injection** | Inyección por constructor vía `@RequiredArgsConstructor` de Lombok |
| **Multi-stage Docker Build** | Imagen de construcción (JDK) separada de la imagen de runtime (JRE) para reducir el tamaño final |
