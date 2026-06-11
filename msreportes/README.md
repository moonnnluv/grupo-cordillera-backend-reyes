# ms-reportes — Microservicio de Reportes
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC

## Descripción
Microservicio responsable de la visualización y gestión de reportes.
Permite generar, consultar y eliminar reportes filtrando por tipo y sucursal.

## Stack
- Java 21 · Spring Boot 3.5.x
- JPA + MySQL
- SpringDoc OpenAPI (Swagger UI)

## Requisitos previos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo con base de datos `basereportes`

## Configuración
Variables de entorno (o valores por defecto en `application.properties`):
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/basereportes
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
```

## Ejecución local
```bash
cd msreportes
./mvnw spring-boot:run
```
Corre en `http://localhost:8083`

## Ejecución con Docker
```bash
docker compose up ms-reportes db-reportes
```

## Endpoints
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/reportes` | Listar todos los reportes |
| POST | `/api/reportes` | Generar un nuevo reporte |
| GET | `/api/reportes/tipo/{tipo}` | Consultar reportes por tipo |
| GET | `/api/reportes/sucursal/{sucursal}` | Consultar reportes por sucursal |
| DELETE | `/api/reportes/{id}` | Eliminar un reporte por ID |

## Ejemplo generación
```json
POST /api/reportes
{
  "tipo": "VENTAS_MENSUAL",
  "sucursal": "Concepción",
  "periodo": "2025-05",
  "contenido": "Resumen de ventas mayo 2025"
}
```
Respuesta:
```json
{
  "id": 7,
  "tipo": "VENTAS_MENSUAL",
  "sucursal": "Concepción",
  "periodo": "2025-05",
  "contenido": "Resumen de ventas mayo 2025",
  "fechaCreacion": "2025-06-01T10:32:00"
}
```

## Patrones implementados
- **Repository Pattern** — `ReporteRepository extends JpaRepository`
- **DTO Pattern** — separación entre entidad JPA y objeto de transferencia expuesto en la API
