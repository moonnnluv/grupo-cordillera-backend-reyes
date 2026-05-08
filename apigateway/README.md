# api-gateway — API Gateway
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC

## Descripción
Puerta de entrada única al sistema. Gestiona routing, validación JWT,
headers de correlación y CORS centralizado.

## Stack
- Java 21 · Spring Boot 3.5.x
- Spring Cloud Gateway (MVC, no reactivo)
- JWT (jjwt 0.11.5) para validación de tokens

## Puerto
`9090`

## Rutas configuradas
| Prefijo | Destino | Puerto |
|---------|---------|--------|
| `/api/datos/**` | ms-datos | 8081 |
| `/api/kpi/**` | ms-kpi | 8082 |
| `/api/reportes/**` | ms-reportes | 8083 |
| `/api/auth/**` | ms-auth | 8084 (público) |
| `/bff/**` | ms-bff | 8085 |

## Ejecución local
```bash
cd apigateway
./mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up api-gateway
```

## Seguridad
- Rutas `/api/auth/**` son públicas (no requieren token)
- Todas las demás rutas requieren header: `Authorization: Bearer <token>`
- El filtro `AuthGatewayFilter` valida el JWT y agrega headers:
  - `X-User-Id` — username del usuario autenticado
  - `X-Correlation-Id` — UUID único por request
  - `X-User-Rol` — rol del usuario

## Variables de entorno
```
MS_DATOS_URL=http://ms-datos:8081
MS_KPI_URL=http://ms-kpi:8082
MS_REPORTES_URL=http://ms-reportes:8083
MS_AUTH_URL=http://ms-auth:8084
MS_BFF_URL=http://ms-bff:8085
```
