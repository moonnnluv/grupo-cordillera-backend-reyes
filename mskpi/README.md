# ms-kpi — Microservicio de KPIs
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC

## Descripción
Microservicio responsable de la gestión de indicadores clave de rendimiento (KPIs).
Permite calcular, registrar y consultar KPIs filtrando por tipo y sucursal.
Implementa el patrón **Factory Method** para la creación de calculadores especializados según el tipo de KPI.

## Stack
- Java 21 · Spring Boot 3.5.x
- JPA + MySQL
- SpringDoc OpenAPI (Swagger UI)

## Requisitos previos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo con base de datos `basekpi`

## Configuración
Variables de entorno (o valores por defecto en `application.properties`):
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/basekpi
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
```

## Ejecución local
```bash
cd mskpi
./mvnw spring-boot:run
```
Corre en `http://localhost:8082`

## Ejecución con Docker
```bash
docker compose up ms-kpi db-kpi
```

## Endpoints
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/kpi` | Listar todos los KPIs |
| POST | `/api/kpi` | Registrar un nuevo KPI |
| POST | `/api/kpi/calcular` | Calcular un KPI usando el Factory |
| GET | `/api/kpi/tipo/{tipo}` | Consultar KPIs por tipo |
| GET | `/api/kpi/sucursal/{sucursal}` | Consultar KPIs por sucursal |
| DELETE | `/api/kpi/{id}` | Eliminar un KPI por ID |

## Ejemplo cálculo
```json
POST /api/kpi/calcular
{
  "tipo": "VENTAS",
  "sucursal": "Valparaíso",
  "periodo": "2025-05"
}
```
Respuesta:
```json
{
  "id": 3,
  "tipo": "VENTAS",
  "sucursal": "Valparaíso",
  "periodo": "2025-05",
  "valor": 87500.0
}
```

## Patrones implementados
- **Factory Method** — `KpiCalculadorFactory` instancia el calculador correcto según el tipo:
  - `VentasCalculador` → tipo `VENTAS`
  - `RentabilidadCalculador` → tipo `RENTABILIDAD`
  - `InventarioCalculador` → tipo `INVENTARIO`
- **Repository Pattern** — `KpiRepository extends JpaRepository`
