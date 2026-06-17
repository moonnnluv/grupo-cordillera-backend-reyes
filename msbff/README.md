# ms-bff — Backend For Frontend
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC 2026

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
# Desde la raíz del monorepo (grupo-cordillera-backend-reyes/), donde vive docker-compose.yml
cd grupo-cordillera-backend-reyes/
docker compose up --build ms-bff
```

El contenedor `ms-bff` depende de `ms-datos`, `ms-kpi` y `ms-reportes` (arrancan en orden declarado). Para levantar todo el sistema completo, omitir el nombre del servicio: `docker compose up --build`.

```bash
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

| Patrón | Clase(s) | Justificación |
|---|---|---|
| **BFF (Backend For Frontend)** | `BffController` | El frontend del panel gerencial necesita datos de tres microservicios distintos. Sin BFF, el frontend haría 3 llamadas paralelas y debería consolidarlas. El BFF reduce esas 3 llamadas a 1, simplificando el cliente y centralizando la lógica de agregación |
| **Circuit Breaker** | `@CircuitBreaker` + Resilience4j | Si ms-datos, ms-kpi o ms-reportes falla, el panel ejecutivo no debe quedar completamente inoperativo. El Circuit Breaker detecta fallos consecutivos y corta el circuito temporalmente, retornando datos degradados en lugar de propagar errores al cliente |
| **Fallback Pattern** | `dashboardFallback()`, `sucursalFallback()` | Provee una respuesta degradada (listas vacías + estado `SERVICIO_NO_DISPONIBLE`) cuando el Circuit Breaker está abierto. Garantiza que el frontend siempre recibe una respuesta válida, aunque sea parcial |
| **Reactive Client** | `WebClient` (WebFlux) | Realiza llamadas HTTP no bloqueantes a los servicios downstream, evitando que un servicio lento bloquee el hilo del servidor mientras espera respuesta |
| **Service Aggregation** | `WebClientConfig` | Define tres beans `WebClient` independientes (uno por servicio downstream), centralizando la configuración de URLs y permitiendo cambiar endpoints sin modificar el código del controlador |
| **Dependency Injection** | Constructores + `@RequiredArgsConstructor` | Inyección por constructor de los tres `WebClient` garantiza que el BFF sea testeable con mocks de cada servicio downstream |
| **Multi-stage Docker Build** | `Dockerfile` | Separa compilación (JDK) de runtime (JRE), reduciendo el tamaño de la imagen final |
