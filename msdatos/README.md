# ms-datos — Microservicio de Datos Organizacionales
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC 2026

Centraliza y almacena métricas e indicadores operacionales provenientes de distintas fuentes de negocio (ventas, inventario, finanzas, e-commerce) para las sucursales de Grupo Cordillera. Reemplaza el proceso manual de consolidación en hojas de cálculo por un repositorio centralizado y consultable vía API.

---

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5.13 |
| Build | Maven 3.9.6 |
| ORM | Spring Data JPA / Hibernate |
| Base de datos | MySQL 8.0 |
| HTTP Client | Spring WebFlux (WebClient) |

---

## Puerto

| Entorno | Puerto |
|---|---|
| Local | `8081` |
| Docker | `8081` (mapeado `8081:8081`) |

---

## Base de datos

- **Motor:** MySQL 8.0
- **Nombre:** `baseorganizacional`
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`

### Entidad principal: `DatoOrganizacional`

| Campo | Tipo | Nulable | Descripción |
|---|---|---|---|
| `id` | Long | No | PK auto-generada |
| `fuente` | String | No | Origen del dato: `VENTAS`, `INVENTARIO`, `FINANZAS`, `ECOMMERCE` |
| `indicador` | String | No | Nombre del indicador (p. ej. `ingresos_mes`) |
| `valor` | Double | No | Valor numérico del indicador |
| `fecha` | LocalDate | No | Fecha del registro |
| `sucursal` | String | Sí | Identificador de sucursal |

---

## Variables de entorno

| Variable | Valor por defecto (local) | Valor Docker |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/baseorganizacional` | `jdbc:mysql://db-datos:3306/baseorganizacional` |
| `SPRING_DATASOURCE_USERNAME` | `root` | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `211822` | `root` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | `com.mysql.cj.jdbc.Driver` | `com.mysql.cj.jdbc.Driver` |

---

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo en `localhost:3306` con la base de datos `baseorganizacional` creada

```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS baseorganizacional;"

# Compilar y ejecutar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8081`.

---

## Ejecución con Docker

```bash
# Desde el directorio msdatos/
docker compose up --build
```

Levanta dos contenedores:
- `db-datos` — MySQL 8.0 con volumen persistente `datos-data`
- `ms-datos` — aplicación Spring Boot, espera a que la BD esté saludable

```bash
docker compose down      # detener
docker compose down -v   # detener y eliminar volúmenes
```

---

## Endpoints

Base path: `/api/datos`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/datos` | Lista todos los registros de datos organizacionales |
| `GET` | `/api/datos/fuente/{fuente}` | Filtra registros por fuente (`VENTAS`, `INVENTARIO`, `FINANZAS`, `ECOMMERCE`) |
| `GET` | `/api/datos/sucursal/{sucursal}` | Filtra registros por sucursal |
| `POST` | `/api/datos` | Crea un nuevo registro de dato organizacional |
| `DELETE` | `/api/datos/{id}` | Elimina un registro por ID |

### Ejemplo de body para `POST /api/datos`

```json
{
  "fuente": "VENTAS",
  "indicador": "ingresos_mes",
  "valor": 15000000.0,
  "fecha": "2026-05-01",
  "sucursal": "Santiago Centro"
}
```

---

## Patrones implementados

| Patrón | Clase(s) | Justificación |
|---|---|---|
| **Repository Pattern** | `DatoOrganizacionalRepository` | Abstrae el acceso a datos detrás de una interfaz. Grupo Cordillera integra múltiples fuentes (ventas, inventario, finanzas, e-commerce); el patrón permite estandarizar el acceso sin que la lógica de negocio dependa del motor de persistencia. Si se migra de MySQL a otro motor, solo se modifica el repositorio |
| **Service Layer** | `DatoOrganizacionalService` / `DatoOrganizacionalServiceImpl` | Desacopla la lógica de negocio del controlador REST. Permite modificar reglas de validación o transformación de datos sin alterar el contrato de la API |
| **Global Exception Handler** | `GlobalExceptionHandler` | Centraliza el manejo de errores con `@RestControllerAdvice`, garantizando respuestas de error consistentes con timestamp en todos los endpoints sin duplicar lógica |
| **Dependency Injection** | Constructores + `@RequiredArgsConstructor` | Inyección por constructor garantiza inmutabilidad de dependencias y facilita pruebas unitarias |
| **Multi-stage Docker Build** | `Dockerfile` | Separa compilación (JDK) de runtime (JRE), reduciendo el tamaño de la imagen final y minimizando la superficie de ataque en producción |
