# api-gateway — API Gateway
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC 2026

## Descripción
Puerta de entrada única al sistema. Gestiona routing, validación JWT, rate limiting, headers de correlación y CORS centralizado. El cliente nunca interactúa directamente con los microservicios; toda comunicación pasa por el Gateway.

## Stack
- Java 21 · Spring Boot 3.5.x
- Spring Cloud Gateway (MVC, no reactivo)
- JWT (jjwt 0.11.5) para validación de tokens
- Bucket4j 8.10.1 para rate limiting

## Puerto
`9090`

## Rutas configuradas
| Prefijo | Destino | Puerto | Autenticación |
|---------|---------|--------|---------------|
| `/api/datos/**` | ms-datos | 8081 | JWT requerido |
| `/api/kpi/**` | ms-kpi | 8082 | JWT requerido |
| `/api/reportes/**` | ms-reportes | 8083 | JWT requerido |
| `/api/auth/**` | ms-auth | 8084 | Público |
| `/bff/**` | ms-bff | 8085 | JWT requerido |

## Ejecución local
```bash
cd apigateway
./mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up api-gateway
```

## Seguridad — JWT
- Rutas `/api/auth/**` son públicas (no requieren token)
- Todas las demás rutas requieren header: `Authorization: Bearer <token>`
- El filtro `AuthGatewayFilter` valida el JWT y agrega headers:
  - `X-User-Id` — username del usuario autenticado
  - `X-User-Rol` — rol del usuario
  - `X-Correlation-Id` — UUID único por request

**Flujo de validación:**
1. Cliente envía `Authorization: Bearer <token>`
2. Gateway intercepta y valida firma, expiración y claims
3. Si válido: pasa la solicitud al microservicio con headers de contexto
4. Si inválido: retorna `401 Unauthorized` sin ejecutar el microservicio

## Seguridad — Rate Limiting (Bucket4j)
- **Límite:** 20 solicitudes por minuto por dirección IP
- **Respuesta al exceder el límite:** `429 Too Many Requests`
- El filtro `RateLimitFilter` se ejecuta antes que la validación JWT (`@Order(1)`)
- Protege los servidores ante sobrecargas y garantiza disponibilidad equitativa

```json
{
  "error": "Too Many Requests",
  "message": "Límite de solicitudes excedido. Intente nuevamente en un minuto."
}
```

## Variables de entorno
```
MS_DATOS_URL=http://ms-datos:8081
MS_KPI_URL=http://ms-kpi:8082
MS_REPORTES_URL=http://ms-reportes:8083
MS_AUTH_URL=http://ms-auth:8084
MS_BFF_URL=http://ms-bff:8085
```

## Patrones implementados

| Patrón | Clase | Justificación |
|--------|-------|---------------|
| **Gateway** | `AuthGatewayFilter`, `RateLimitFilter` | Centraliza el enrutamiento, la seguridad y el control de tráfico en un único punto de entrada, evitando que cada microservicio deba implementar estas responsabilidades por separado |
| **Filter Chain** | `OncePerRequestFilter` | Permite componer comportamientos transversales (autenticación, rate limiting, CORS) de forma modular sin acoplarlos a la lógica de negocio |
| **Rate Limiter** | `RateLimitFilter` + Bucket4j | Protege la plataforma ante sobrecargas mediante el algoritmo Token Bucket: cada IP dispone de 20 tokens por minuto que se recargan intervalamente |
| **Correlation ID** | `AuthGatewayFilter` | Agrega un UUID único por request que permite rastrear una solicitud a través de todos los microservicios en los logs |
| **CORS centralizado** | `CorsConfig` | Define la política de origen permitido (`http://localhost:5173`) en un solo lugar, sin repetir configuración en cada microservicio |
