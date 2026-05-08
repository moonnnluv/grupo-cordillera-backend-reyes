# ms-bff — Backend For Frontend
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC

## Descripción
Capa BFF que agrega datos de `ms-datos`, `ms-kpi` y `ms-reportes` en una sola
respuesta optimizada para el frontend. Implementa Circuit Breaker con Resilience4j.

## Stack
- Java 21 · Spring Boot 3.5.x
- WebClient (Spring WebFlux) para comunicación entre microservicios
- Resilience4j — Circuit Breaker

## Requisitos previos
- Java 21
- Maven 3.9+
- `ms-datos`, `ms-kpi` y `ms-reportes` corriendo

## Configuración
```
MS_DATOS_URL=http://localhost:8081
MS_KPI_URL=http://localhost:8082
MS_REPORTES_URL=http://localhost:8083
```

## Ejecución local
```bash
cd msbff
./mvnw spring-boot:run
```
Corre en `http://localhost:8085`

## Ejecución con Docker
```bash
docker compose up ms-bff
```

## Endpoints
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/bff/dashboard` | Dashboard consolidado todas las sucursales |
| GET | `/bff/dashboard/sucursal/{sucursal}` | Dashboard filtrado por sucursal |

## Circuit Breaker
Si algún microservicio no responde, el fallback retorna:
```json
{ "datos": [], "kpis": [], "reportes": [], "estado": "SERVICIO_NO_DISPONIBLE" }
```
Estados: `CLOSED` (normal) → `OPEN` (fallo) → `HALF-OPEN` (recuperación)

## Patrones implementados
- **BFF Pattern** — agrega múltiples respuestas en una sola
- **Circuit Breaker** — `@CircuitBreaker(name = "bff-dashboard", fallbackMethod = "dashboardFallback")`
