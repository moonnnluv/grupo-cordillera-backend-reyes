# ms-auth — Microservicio de Autenticación y Autorización
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC 2026

Gestiona el registro de usuarios, autenticación con JWT y control de acceso basado en roles (RBAC) para Grupo Cordillera. Emite tokens firmados con HS256 que los demás servicios validan mediante el API Gateway.

---

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Build | Maven 3.9.6 |
| Seguridad | Spring Security 6 |
| JWT | JJWT 0.11.5 (HS256) |
| ORM | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8.0 |

---

## Puerto

| Entorno | Puerto |
|---|---|
| Local | `8084` |
| Docker | `8084` (mapeado `8084:8084`) |

---

## Base de datos

- **Motor:** MySQL 8.0
- **Nombre:** `baseauth`
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`

### Entidad: `Usuario` → tabla `usuarios`

| Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT | Clave surrogate |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL | Identificador de login |
| `password` | VARCHAR(255) | NOT NULL | Hash BCrypt |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | Correo electrónico |
| `rol` | VARCHAR(30) | NOT NULL | `ADMIN_GENERAL`, `ADMIN_SUCURSAL` o `VENDEDOR` |
| `enabled` | BOOLEAN | NOT NULL, DEFAULT TRUE | Cuenta activa |

---

## Variables de entorno

| Variable | Valor por defecto (local) | Valor Docker |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/baseauth?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` | `jdbc:mysql://db-auth:3306/baseauth` |
| `SPRING_DATASOURCE_USERNAME` | `root` | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `211822` | `root` |
| `jwt.secret` | `bXlTdXBlclNlY3JldEtleVBhcmFHcnVwb0NvcmRpbGxlcmEyMDI1RFNZMTEwNg==` | (mismo) |
| `jwt.expiration` | `3600000` (1 hora en ms) | (mismo) |

---

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo en `localhost:3306` con la base de datos `baseauth` creada

```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS baseauth;"

# Compilar y ejecutar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8084`.

---

## Ejecución con Docker

```bash
# Desde el directorio msauth/
docker compose up --build
```

Levanta dos contenedores:
- `db-auth` — MySQL 8.0 con healthcheck (`mysqladmin ping`, intervalo 10 s, 10 reintentos)
- `ms-auth` — aplicación Spring Boot, espera a que la BD esté saludable

```bash
docker compose down      # detener
docker compose down -v   # detener y eliminar volúmenes
```

---

## Endpoints

Base path: `/api/auth` (público — no requiere token)

| Método | Ruta | Descripción | Código de respuesta |
|---|---|---|---|
| `POST` | `/api/auth/login` | Autentica al usuario y devuelve un JWT | `200 OK` |
| `POST` | `/api/auth/register` | Registra un nuevo usuario con rol asignado | `201 Created` |

### `POST /api/auth/login`

**Request body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "username": "string",
    "email": "string",
    "role": "ADMIN_GENERAL"
  }
}
```

### `POST /api/auth/register`

**Request body:**
```json
{
  "username": "string",
  "password": "string",
  "email": "string",
  "rol": "ADMIN_GENERAL | ADMIN_SUCURSAL | VENDEDOR"
}
```
> El campo `rol` es opcional; si se omite, se asigna `VENDEDOR` por defecto.

**Response `201 Created`:**
```json
{ "message": "Usuario registrado exitosamente" }
```

**Response `400 Bad Request`** (username/email duplicado o rol inválido):
```json
{ "message": "El username ya está en uso" }
```

---

## Configuración JWT

| Propiedad | Valor |
|---|---|
| Algoritmo | HS256 (HMAC SHA-256) |
| Claim `sub` | `username` del usuario |
| Claim `rol` | rol del usuario (p. ej. `ADMIN_GENERAL`) |
| Expiración | 1 hora (`jwt.expiration=3600000`) |
| Variable de clave | `jwt.secret` (Base64) |

---

## Configuración de seguridad

| Ruta | Acceso |
|---|---|
| `/api/auth/**` | Público (`permitAll`) |
| Cualquier otra ruta | Requiere JWT válido en `Authorization: Bearer <token>` |

- CSRF deshabilitado (API stateless)
- Gestión de sesión: `STATELESS`
- Codificación de contraseñas: **BCrypt**
- Filtro `JwtAuthenticationFilter` ejecuta antes de `UsernamePasswordAuthenticationFilter`

---

## Patrones implementados

| Patrón | Clase(s) | Justificación |
|---|---|---|
| **DTO** | `LoginRequest`, `RegisterRequest`, `AuthResponse` | Separa los contratos de la API de la entidad de dominio. El campo `password` nunca se expone en las respuestas; los campos de entrada y salida son independientes de la estructura de la base de datos, protegiendo la información sensible |
| **Repository Pattern** | `UsuarioRepository` | Encapsula el acceso a datos de usuarios. Centraliza las consultas por username y validaciones de unicidad, sin exponer SQL al servicio |
| **Service Layer** | `CustomUserDetailsService` | Implementa `UserDetailsService` de Spring Security, mapeando la entidad `Usuario` a `UserDetails`. Desacopla Spring Security de la estructura interna de la BD |
| **Security Filter** | `JwtAuthenticationFilter` | Extiende `OncePerRequestFilter` para extraer y validar el token en cada request. Centraliza la autenticación en un único componente sin repetir lógica en cada endpoint |
| **JWT Utility** | `JwtUtils` | Centraliza la generación, validación y extracción de claims JWT. Si se cambia el algoritmo o la librería JWT, solo se modifica esta clase |
| **Global Exception Handler** | `@RestControllerAdvice` | Devuelve respuestas de error estandarizadas ante credenciales inválidas, usernames duplicados o roles no reconocidos |
| **Dependency Injection** | Constructores + `@RequiredArgsConstructor` | Inyección por constructor garantiza inmutabilidad y facilita pruebas unitarias con mocks de Spring Security |
| **Multi-stage Docker Build** | `Dockerfile` | Separa compilación (JDK) de runtime (JRE), reduciendo el tamaño de la imagen y la superficie de ataque |
