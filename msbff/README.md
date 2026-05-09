# ms-bff — Backend For Frontend

Capa de agregación que consolida en una sola respuesta los datos provenientes de ms-datos, ms-kpi y ms-reportes. Reduce la cantidad de llamadas que el frontend necesita realizar y aplica Circuit Breaker para tolerar fallos parciales en los servicios downstream.

---

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Build | Maven 3.9.6 |
| HTTP Client | Spring WebFlux (WebClient) |
| Circuit Breaker | Resilience4j 2.2.0 |
| AOP | Spring AOP (requerido por Resilience4j) |

---

## Puerto

| Entorno | Puerto |
|---|---|
| Local | `8085` |
| Docker | `8085` (mapeado `8085:8085`) |

---

## Variables de entorno

| Variable | Valor por defecto (local) | Valor Docker |
|---|---|---|
| `MS_DATOS_URL` | `http://localhost:8081` | `http://ms-datos:8081` |
| `MS_KPI_URL` | `http://localhost:8082` | `http://ms-kpi:8082` |
| `MS_REPORTES_URL` | `http://localhost:8083` | `http://ms-reportes:8083` |

---

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- ms-datos, ms-kpi y ms-reportes corriendo en sus respectivos puertos

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8085`.

---

## Ejecución con Docker

```bash
# Desde el directorio msbff/
docker compose up --build
```

El contenedor `ms-bff` depende de `ms-datos`, `ms-kpi` y `ms-reportes` (arrancan en orden declarado).

```bash
# Detener
docker compose down
```

---

## Endpoints

Base path: `/bff`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/bff/dashboard` | Agrega datos, KPIs y reportes de todas las sucursales |
| `GET` | `/bff/dashboard/sucursal/{sucursal}` | Agrega datos, KPIs y reportes filtrados por sucursal |

### Llamadas downstream por endpoint

| Endpoint BFF | Servicio | Ruta downstream |
|---|---|---|
| `/bff/dashboard` | ms-datos | `GET /api/datos` |
| `/bff/dashboard` | ms-kpi | `GET /api/kpi` |
| `/bff/dashboard` | ms-reportes | `GET /api/reportes` |
| `/bff/dashboard/sucursal/{s}` | ms-datos | `GET /api/datos/sucursal/{s}` |
| `/bff/dashboard/sucursal/{s}` | ms-kpi | `GET /api/kpi/sucursal/{s}` |
| `/bff/dashboard/sucursal/{s}` | ms-reportes | `GET /api/reportes/sucursal/{s}` |

### Estructura de respuesta (`DashboardResponse`)

```json
{
  "datos":    [ ...DatoOrganizacional... ],
  "kpis":     [ ...Kpi... ],
  "reportes": [ ...Reporte... ],
  "estado":   "OK"
}
```

**Respuesta de fallback** (Circuit Breaker abierto):
```json
{
  "datos":    [],
  "kpis":     [],
  "reportes": [],
  "estado":   "SERVICIO_NO_DISPONIBLE"
}
```

---

## Configuración del Circuit Breaker

| Parámetro | `bff-dashboard` | `bff-sucursal` |
|---|---|---|
| Endpoint protegido | `GET /bff/dashboard` | `GET /bff/dashboard/sucursal/{s}` |
| Sliding window size | 5 | 5 |
| Failure rate threshold | 50 % | 50 % |
| Wait duration (OPEN) | 10 s | 10 s |
| Fallback method | `dashboardFallback()` | `sucursalFallback()` |

**Estados:** `CLOSED` → `OPEN` (al superar el umbral) → `HALF-OPEN` (sondeo de recuperación) → `CLOSED`

---

## Patrones implementados

| Patrón | Descripción |
|---|---|
| **BFF (Backend For Frontend)** | Agrega respuestas de tres microservicios en una sola llamada optimizada para el frontend |
| **Circuit Breaker** | `@CircuitBreaker` de Resilience4j con métodos de fallback que devuelven listas vacías y estado `SERVICIO_NO_DISPONIBLE` |
| **Fallback Pattern** | Respuesta degradada que evita propagar errores al cliente cuando un servicio downstream falla |
| **Reactive Client** | `WebClient` (WebFlux) para llamadas HTTP no bloqueantes a los servicios downstream |
| **Service Aggregation** | `WebClientConfig` define tres beans `WebClient` independientes, uno por servicio downstream |
| **Dependency Injection** | Inyección por constructor vía `@RequiredArgsConstructor` de Lombok |
| **Multi-stage Docker Build** | Imagen de construcción (JDK) separada de la imagen de runtime (JRE) |
