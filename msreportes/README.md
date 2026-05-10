# ms-reportes — Microservicio de Reportes
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC 2026

Gestiona el almacenamiento y la recuperación de reportes de negocio para las distintas áreas y sucursales de Grupo Cordillera. Proporciona el panel de control que la alta dirección requiere para tomar decisiones oportunas, reemplazando la generación manual de informes en hojas de cálculo.

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
| `titulo` | String | Título descriptivo del reporte |
| `tipo` | String | Tipo de reporte: `VENTAS`, `INVENTARIO`, `FINANCIERO`, etc. |
| `sucursal` | String | Identificador de sucursal (nullable para reportes globales) |
| `contenido` | String | Contenido o resumen del reporte |
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

```bash
docker compose down      # detener
docker compose down -v   # detener y eliminar volúmenes
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
  "titulo": "Reporte Mensual de Ventas",
  "tipo": "VENTAS",
  "sucursal": "Concepción",
  "contenido": "Resumen mensual de ventas: ingresos totales $12.500.000",
  "fecha": "2026-05-01"
}
```

---

## Patrones implementados

| Patrón | Clase(s) | Justificación |
|---|---|---|
| **Repository Pattern** | `ReporteRepository` | Abstrae el acceso a datos. La alta dirección requiere distintas vistas de los reportes (por tipo, por sucursal, combinadas); el repositorio encapsula estas consultas sin exponer SQL al resto de la aplicación |
| **Service Layer** | `ReporteService` / `ReporteServiceImpl` | Desacopla la lógica de negocio del controlador REST. Permite agregar validaciones o transformaciones de reportes sin alterar el contrato de la API |
| **Global Exception Handler** | `GlobalExceptionHandler` | Centraliza el manejo de errores con `@RestControllerAdvice`, garantizando respuestas de error consistentes con timestamp en todos los endpoints |
| **Dependency Injection** | Constructores + `@RequiredArgsConstructor` | Inyección por constructor garantiza inmutabilidad de dependencias y facilita pruebas unitarias |
| **Multi-stage Docker Build** | `Dockerfile` | Separa compilación (JDK) de runtime (JRE), reduciendo el tamaño de la imagen final y minimizando la superficie de ataque |
