# ms-auth — Microservicio de Autenticación y Autorización

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
# Detener
docker compose down

# Detener y eliminar volúmenes
docker compose down -v
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

| Patrón | Descripción |
|---|---|
| **DTO** | `LoginRequest`, `RegisterRequest`, `AuthResponse` separan los contratos de API de la entidad de dominio |
| **Repository Pattern** | `UsuarioRepository` extiende `JpaRepository`; consultas `findByUsername`, `existsByUsername`, `existsByEmail` |
| **Service Layer** | `CustomUserDetailsService` implementa `UserDetailsService`; mapea `Usuario` a `UserDetails` de Spring Security |
| **Security Filter** | `JwtAuthenticationFilter` extiende `OncePerRequestFilter`; extrae y valida el token en cada request |
| **JWT Utility** | `JwtUtils` centraliza generación, validación y extracción de claims |
| **Global Exception Handler** | `@RestControllerAdvice` devuelve respuestas de error estandarizadas |
| **Dependency Injection** | Inyección por constructor vía `@RequiredArgsConstructor` de Lombok |
| **Multi-stage Docker Build** | Imagen de construcción (JDK) separada de la imagen de runtime (JRE) |
