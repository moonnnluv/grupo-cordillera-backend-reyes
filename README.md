# Grupo Cordillera

Plataforma de monitoreo inteligente para el desempeño organizacional.
DSY1106 Desarrollo Fullstack III · DuocUC 2026

---

## Microservicios

| Servicio | Puerto | Base de datos | Descripción |
| --- | --- | --- | --- |
| ms-datos | 8081 | baseorganizacional | Centraliza métricas operacionales |
| ms-kpi | 8082 | basekpi | Calcula indicadores de desempeño |
| ms-reportes | 8083 | basereportes | Gestiona reportes para la dirección |
| ms-auth | 8084 | baseauth | Autenticación JWT y control de roles |
| ms-bff | 8085 | — | Agrega respuestas para el frontend |
| api-gateway | 9090 | — | Punto de entrada único, JWT + Rate Limiting |

---

## Stack

- Java 21 + Spring Boot 3.5
- MySQL 8.0 + Spring Data JPA
- Spring Cloud Gateway (MVC)
- Resilience4j (Circuit Breaker)
- Bucket4j (Rate Limiting)
- Docker + Docker Compose
- React (frontend)

---

## Cómo levantar el sistema

```bash
# Desde la raíz del proyecto
docker compose up --build
```

Esto levanta todos los contenedores en orden correcto gracias a los healthchecks configurados.

---

## Inicio desde cero (reset completo)

Sigue estos pasos en orden para hacer un reset limpio y dejar el sistema funcionando desde cero.

**Paso 1 — Bajar todo y eliminar volúmenes e imágenes**

```bash
docker compose down -v --rmi all
```

**Paso 2 — Volver a levantar desde cero**

```bash
docker compose up --build
```

**Paso 3 — Esperar que todos los contenedores estén `healthy`, luego correr el seed**

```powershell
Get-Content datos_iniciales.sql | docker exec -i db-auth mysql -uroot -proot --force baseauth
Get-Content datos_iniciales.sql | docker exec -i db-datos mysql -uroot -proot --force baseorganizacional
Get-Content datos_iniciales.sql | docker exec -i db-kpi mysql -uroot -proot --force basekpi
Get-Content datos_iniciales.sql | docker exec -i db-reportes mysql -uroot -proot --force basereportes
```

> Los errores `ERROR 1049` y `ERROR 1146` que aparecen son normales e ignorables — ver sección **Datos iniciales (seed)** más abajo.

**Paso 4 — Levantar el frontend**

```bash
cd grupo-cordillera-frontend-reyes/frontend
npm run dev
```

El frontend quedará disponible en `http://localhost:5173`.

---

## Datos iniciales (seed)

Después de levantar el sistema con `docker compose up`, ejecuta el archivo `datos_iniciales.sql` en cada contenedor MySQL para poblar las bases de datos.

> **Requisito:** espera a que todos los servicios estén `healthy` antes de correr el seed — Spring Boot crea las tablas al iniciar, por lo que el seed debe ejecutarse después.

**Bash / Mac / Linux**

```bash
# baseauth — usuarios de prueba
docker exec -i db-auth mysql -uroot -proot --force < datos_iniciales.sql

# baseorganizacional — datos organizacionales
docker exec -i db-datos mysql -uroot -proot --force < datos_iniciales.sql

# basekpi — KPIs
docker exec -i db-kpi mysql -uroot -proot --force < datos_iniciales.sql

# basereportes — reportes
docker exec -i db-reportes mysql -uroot -proot --force < datos_iniciales.sql
```

**PowerShell (Windows)**

```powershell
# baseauth — usuarios de prueba
Get-Content datos_iniciales.sql | docker exec -i db-auth mysql -uroot -proot --force baseauth

# baseorganizacional — datos organizacionales
Get-Content datos_iniciales.sql | docker exec -i db-datos mysql -uroot -proot --force baseorganizacional

# basekpi — KPIs
Get-Content datos_iniciales.sql | docker exec -i db-kpi mysql -uroot -proot --force basekpi

# basereportes — reportes
Get-Content datos_iniciales.sql | docker exec -i db-reportes mysql -uroot -proot --force basereportes
```

> **Nota:** durante la ejecución pueden aparecer errores `ERROR 1049 (42000): Unknown database` y `ERROR 1146 (42S02): Table doesn't exist`. Esto es **normal e ignorable** — el archivo SQL contiene sentencias `USE` para todas las bases, pero cada contenedor solo reconoce la suya propia. El flag `--force` hace que MySQL continúe de todos modos, y cada base de datos inserta únicamente sus propios datos correctamente.

**Usuarios de prueba** (password: `Admin123!`):

| Username | Rol |
| --- | --- |
| `admin` | ADMIN_GENERAL |
| `jefa.santiago` | ADMIN_SUCURSAL |
| `vendedor1` | VENDEDOR |
| `vendedor2` | VENDEDOR |

---

## Estrategia de branching: Git Flow

### Ramas principales

| Rama | Propósito | Protección |
| --- | --- | --- |
| `main` | Producción — código estable y desplegable | No se hace push directo; solo merge desde `develop` |
| `develop` | Integración continua — rama base del proyecto | Recibe merges desde ramas `feature/` |

### Ramas de trabajo

| Rama | Propósito |
| --- | --- |
| `feature/backend/ale-reyes` | Desarrollo del backend (microservicios Spring Boot) |
| `feature/frontend/ale-reyes` | Desarrollo del frontend (React) |

### Convención de nombres

```
feature/<área>/<desarrollador>
```

Ejemplos: `feature/backend/ale-reyes`, `feature/frontend/ale-reyes`

### Flujo de trabajo

```
feature/<área>/<desarrollador>
        ↓  (merge al completar funcionalidad)
      develop
        ↓  (merge al cierre de entrega)
        main
```

### Reglas

- No se hace push directo a `main` ni a `develop`
- Los commits deben ser descriptivos e indicar el tipo de cambio:
  - `feat:` nueva funcionalidad
  - `fix:` corrección de error
  - `docs:` cambios en documentación
  - `refactor:` refactorización sin cambio de funcionalidad
- Un merge a `develop` requiere que la funcionalidad esté probada localmente

---

## Arquetipos Maven

Todos los microservicios fueron generados usando **Spring Initializr** (`start.spring.io`), que es el estándar de la comunidad Spring Boot y equivale funcionalmente a un arquetipo Maven especializado.

### Base común de todos los microservicios

| Configuración | Valor |
| --- | --- |
| Build tool | Maven 3.9.6 |
| Parent POM | `spring-boot-starter-parent 3.5.13` |
| Java | 21 |
| Empaquetado | JAR |

El `spring-boot-starter-parent` actúa como POM base que provee:
- Gestión de versiones de dependencias Spring Boot
- Configuración del compilador Java 21
- Plugin `spring-boot-maven-plugin` para generar JARs ejecutables

### Cómo generar un nuevo microservicio con la misma base

```bash
mvn archetype:generate \
  -DgroupId=com.cordillera \
  -DartifactId=ms-nuevo \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4
```

Luego reemplazar el `pom.xml` generado por la estructura base:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.13</version>
</parent>
```

O bien usar Spring Initializr directamente:

```bash
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,mysql \
  -d javaVersion=21 \
  -d bootVersion=3.5.13 \
  -d artifactId=ms-nuevo \
  -o ms-nuevo.zip
```

---

## Análisis de patrones y arquetipos

### Decisión: Microservicios vs Monolítico

| Criterio | Monolítica | Microservicios (elegido) |
| --- | --- | --- |
| Estructura | Toda la app en un solo proyecto | Servicios independientes por dominio |
| Escalabilidad | Escala todo o nada | Escala cada servicio por separado |
| Acoplamiento | Alta dependencia entre módulos | Bajo acoplamiento entre servicios |
| Despliegue | Lento y riesgoso | Independiente y ágil |
| Mantenimiento | Difícil a largo plazo | Equipos paralelos por dominio |

Grupo Cordillera requiere consolidar información de múltiples sistemas con distintos ritmos de actualización. La arquitectura de microservicios permite escalar y mantener cada módulo (datos, KPI, reportes) de forma autónoma sin afectar el funcionamiento global.

### Patrones implementados

| Patrón | Microservicio(s) | Justificación |
| --- | --- | --- |
| **Repository Pattern** | ms-datos, ms-kpi, ms-reportes, ms-auth | Abstrae el acceso a datos detrás de una interfaz. Permite cambiar el motor de persistencia sin impactar la lógica de negocio. Estandariza el acceso a la información de ventas, inventarios y finanzas de Grupo Cordillera |
| **Factory Method** | ms-kpi | Determina qué calculadora de KPI instanciar según el tipo recibido. Cumple el principio Open/Closed: agregar un nuevo tipo de KPI solo requiere una nueva clase, sin modificar el código existente |
| **Strategy** | ms-kpi | Encapsula cada algoritmo de cálculo (ventas, rentabilidad, inventario) en su propia clase intercambiable. El cliente (KpiService) no conoce la implementación concreta |
| **Circuit Breaker** | ms-bff | Protege el sistema ante fallos en microservicios downstream. Si ms-datos falla, el dashboard devuelve datos degradados en lugar de propagar el error, garantizando disponibilidad parcial |
| **BFF (Backend For Frontend)** | ms-bff | Consolida 3 llamadas de microservicios en 1 respuesta para el frontend, reduciendo la complejidad del cliente |
| **Gateway** | api-gateway | Punto de entrada único que centraliza routing, JWT, rate limiting y CORS, evitando que cada microservicio implemente estas responsabilidades |
| **Rate Limiter** | api-gateway | Controla el volumen de solicitudes por IP (20 req/min) para proteger los servidores ante sobrecargas |

---

## Referencias

- Fowler, M. (2002). *Patterns of enterprise application architecture*. Addison-Wesley.
- Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design patterns*. Addison-Wesley.
- Newman, S. (2015). *Building microservices*. O'Reilly Media.
- Poblete, V. (2026). Guía de arquitecturas de software — Fullstack III [Material de cátedra]. DuocUC.