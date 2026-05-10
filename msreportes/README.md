# ms-reportes — Microservicio de Reportes

Gestiona el almacenamiento y la recuperación de reportes de negocio para las distintas áreas y sucursales de Grupo Cordillera. Provee filtrado por tipo de reporte y por sucursal.

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
| Local | `8083` |
| Docker | `8083` (mapeado `8083:8083`) |

---

## Base de datos

- **Motor:** MySQL 8.0
- **Nombre:** `basereportes`
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`
- **SQL logging:** habilitado (`spring.jpa.show-sql=true`)

### Entidad principal: `Reporte`

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | PK auto-generada |
| `tipo` | String | Tipo de reporte: `VENTAS`, `INVENTARIO`, `FINANCIERO`, etc. |
| `sucursal` | String | Identificador de sucursal |
| `contenido` | String / Text | Contenido o resumen del reporte |
| `fecha` | LocalDate | Fecha del reporte |

---

## Variables de entorno

| Variable | Valor por defecto (local) | Valor Docker |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/basereportes` | `jdbc:mysql://db-reportes:3306/basereportes` |
| `SPRING_DATASOURCE_USERNAME` | `root` | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `211822` | `root` |
| `MYSQL_ROOT_PASSWORD` | — | `root` |
| `MYSQL_DATABASE` | — | `basereportes` |

---

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo en `localhost:3306` con la base de datos `basereportes` creada

```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS basereportes;"

# Compilar y ejecutar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8083`.

---

## Ejecución con Docker

```bash
# Desde el directorio msreportes/
docker compose up --build
```

Levanta dos contenedores:
- `db-reportes` — MySQL 8.0 con volumen persistente `reportes-data`
- `ms-reportes` — aplicación Spring Boot, espera a que la BD esté saludable

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

Base path: `/api/reportes`

| Método | Ruta | Descripción | Código de respuesta |
|---|---|---|---|
| `GET` | `/api/reportes` | Lista todos los reportes | `200 OK` |
| `GET` | `/api/reportes/tipo/{tipo}` | Filtra reportes por tipo (`VENTAS`, `INVENTARIO`, `FINANCIERO`, etc.) | `200 OK` |
| `GET` | `/api/reportes/sucursal/{sucursal}` | Filtra reportes por sucursal | `200 OK` |
| `GET` | `/api/reportes/tipo/{tipo}/sucursal/{sucursal}` | Filtra reportes por tipo y sucursal | `200 OK` |
| `POST` | `/api/reportes` | Crea y guarda un nuevo reporte | `201 Created` |
| `DELETE` | `/api/reportes/{id}` | Elimina un reporte por ID | `204 No Content` |

### Ejemplo de body para `POST /api/reportes`

```json
{
  "tipo": "VENTAS",
  "sucursal": "Concepción",
  "contenido": "Resumen mensual de ventas: ingresos totales $12.500.000",
  "fecha": "2026-05-01"
}
```

---

## Patrones implementados

| Patrón | Descripción |
|---|---|
| **Repository Pattern** | `ReporteRepository` extiende `JpaRepository` con consultas personalizadas por tipo, sucursal y combinación de ambos |
| **Service Layer** | Interfaz `ReporteService` + implementación `ReporteServiceImpl`; desacopla la lógica de negocio del controlador |
| **Global Exception Handler** | `@RestControllerAdvice` en `GlobalExceptionHandler`; respuestas de error estandarizadas |
| **Dependency Injection** | Inyección por constructor vía `@RequiredArgsConstructor` de Lombok |
| **Multi-stage Docker Build** | Imagen de construcción (JDK) separada de la imagen de runtime (JRE) para reducir el tamaño final |
