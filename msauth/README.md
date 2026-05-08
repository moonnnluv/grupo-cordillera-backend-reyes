# ms-auth — Microservicio de Autenticación
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC

## Descripción
Microservicio responsable del registro y autenticación de usuarios mediante JWT.
Gestiona tres roles: `ADMIN_GENERAL`, `ADMIN_SUCURSAL`, `VENDEDOR`.

## Stack
- Java 21 · Spring Boot 3.5.x
- Spring Security + JWT (jjwt 0.11.5)
- BCrypt para hashing de contraseñas
- JPA + MySQL

## Requisitos previos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo con base de datos `baseauth`

## Configuración
Variables de entorno (o valores por defecto en `application.properties`):
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/baseauth
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
```

## Ejecución local
```bash
cd msauth
./mvnw spring-boot:run
```
Corre en `http://localhost:8084`

## Ejecución con Docker
```bash
docker compose up ms-auth db-auth
```

## Endpoints
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registrar nuevo usuario |
| POST | `/api/auth/login` | Iniciar sesión, retorna JWT |

## Ejemplo login
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```
Respuesta:
```json
{
  "token": "eyJhbGci...",
  "user": { "username": "admin", "email": "admin@cordillera.cl", "role": "ADMIN_GENERAL" }
}
```

## Patrones implementados
- **Repository Pattern** — `UsuarioRepository extends JpaRepository`
- **BCrypt** — hash unidireccional, nunca se almacena la contraseña en texto plano
