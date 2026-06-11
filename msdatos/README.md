# ms-datos — Microservicio de Datos Organizacionales
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC

## Descripción
Microservicio responsable de la gestión de datos organizacionales.
Permite registrar, consultar y eliminar datos filtrando por fuente de origen y sucursal.

## Stack
- Java 21 · Spring Boot 3.5.x
- JPA + MySQL
- SpringDoc OpenAPI (Swagger UI)

## Requisitos previos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo con base de datos `baseorganizacional`

## Configuración
Variables de entorno (o valores por defecto en `application.properties`):
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/baseorganizacional
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
```

## Ejecución local
```bash
cd msdatos
./mvnw spring-boot:run
```
Corre en `http://localhost:8081`

## Ejecución con Docker
```bash
docker compose up ms-datos db-datos
```

## Endpoints
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/datos` | Listar todos los datos |
| POST | `/api/datos` | Registrar un nuevo dato |
| GET | `/api/datos/fuente/{fuente}` | Consultar datos por fuente |
| GET | `/api/datos/sucursal/{sucursal}` | Consultar datos por sucursal |
| DELETE | `/api/datos/{id}` | Eliminar un dato por ID |

## Ejemplo registro
```json
POST /api/datos
{
  "fuente": "ERP",
  "sucursal": "Santiago Centro",
  "valor": 152300.0,
  "fecha": "2025-06-01"
}
```
Respuesta:
```json
{
  "id": 1,
  "fuente": "ERP",
  "sucursal": "Santiago Centro",
  "valor": 152300.0,
  "fecha": "2025-06-01"
}
```

## Patrones implementados
- **Repository Pattern** — `DatoRepository extends JpaRepository`
- **DTO Pattern** — separación entre entidad JPA y objeto de transferencia expuesto en la API
