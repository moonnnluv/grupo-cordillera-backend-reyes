# ms-kpi — Microservicio de Gestión de KPIs

Gestiona y calcula Key Performance Indicators (KPIs) para las distintas áreas de negocio y sucursales de Grupo Cordillera. Aplica fórmulas de cálculo específicas por tipo de KPI mediante un patrón Factory + Strategy.

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
| Local | `8082` |
| Docker | `8082` (mapeado `8082:8082`) |

---

## Base de datos

- **Motor:** MySQL 8.0
- **Nombre:** `basekpi`
- **DDL:** `spring.jpa.hibernate.ddl-auto=update`

### Entidad principal: `Kpi`

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | PK auto-generada |
| `tipo` | String | Tipo de KPI: `VENTAS`, `RENTABILIDAD`, `INVENTARIO` |
| `nombre` | String | Nombre del KPI |
| `valorBase` | Double | Valor de entrada para el cálculo |
| `valorCalculado` | Double | Resultado de aplicar la fórmula del tipo |
| `unidad` | String | Unidad del resultado (p. ej. `%`, `CLP`, `unidades/día`) |
| `sucursal` | String | Identificador de sucursal |
| `fecha` | LocalDate | Fecha de cálculo |

---

## Variables de entorno

| Variable | Valor por defecto (local) | Valor Docker |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/basekpi` | `jdbc:mysql://db-kpi:3306/basekpi` |
| `SPRING_DATASOURCE_USERNAME` | `root` | `root` |
| `SPRING_DATASOURCE_PASSWORD` | `211822` | `root` |
| `MYSQL_ROOT_PASSWORD` | — | `root` |
| `MYSQL_DATABASE` | — | `basekpi` |

---

## Ejecución local

### Prerrequisitos
- Java 21
- Maven 3.9+
- MySQL 8.0 corriendo en `localhost:3306` con la base de datos `basekpi` creada

```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS basekpi;"

# Compilar y ejecutar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8082`.

---

## Ejecución con Docker

```bash
# Desde el directorio mskpi/
docker compose up --build
```

Levanta dos contenedores:
- `db-kpi` — MySQL 8.0 con volumen persistente `kpi-data`
- `ms-kpi` — aplicación Spring Boot, espera a que la BD esté saludable

Para detener y eliminar los contenedores:

```bash
docker compose down
```

Para eliminar también los volúmenes:

```bash
docker compose down -v
```

---

## Endpoints

Base path: `/api/kpi`

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/kpi` | Lista todos los KPIs |
| `GET` | `/api/kpi/tipo/{tipo}` | Filtra KPIs por tipo (`VENTAS`, `RENTABILIDAD`, `INVENTARIO`) |
| `GET` | `/api/kpi/sucursal/{sucursal}` | Filtra KPIs por sucursal |
| `GET` | `/api/kpi/tipo/{tipo}/sucursal/{sucursal}` | Filtra KPIs por tipo y sucursal |
| `POST` | `/api/kpi` | Crea y guarda un KPI con valores ya calculados |
| `DELETE` | `/api/kpi/{id}` | Elimina un KPI por ID |
| `POST` | `/api/kpi/calcular` | Calcula automáticamente el KPI según su tipo y lo persiste |

### Parámetros de `POST /api/kpi/calcular`

| Query param | Tipo | Descripción |
|---|---|---|
| `tipo` | String | Tipo de KPI (`VENTAS`, `RENTABILIDAD`, `INVENTARIO`) |
| `nombre` | String | Nombre del KPI |
| `valorBase` | Double | Valor de entrada para la fórmula |
| `sucursal` | String | Identificador de sucursal |

Ejemplo:
```
POST /api/kpi/calcular?tipo=VENTAS&nombre=margen&valorBase=100000&sucursal=Valparaiso
```

### Fórmulas de cálculo por tipo

| Tipo | Fórmula | Unidad |
|---|---|---|
| `VENTAS` | `valorBase × 0.35` | `%` |
| `RENTABILIDAD` | `valorBase × 1.15` | `CLP` |
| `INVENTARIO` | `valorBase / 30.0` | `unidades/día` |

---

## Patrones implementados

| Patrón | Descripción |
|---|---|
| **Factory Pattern** | `KpiCalculatorFactory` selecciona en tiempo de ejecución la implementación de calculadora según el tipo de KPI |
| **Strategy Pattern** | Interfaz `KpiCalculator` con implementaciones `SalesKpiCalculator`, `FinanceKpiCalculator` e `InventoryKpiCalculator`; cada una encapsula su propia fórmula |
| **Repository Pattern** | `KpiRepository` extiende `JpaRepository` con consultas personalizadas por tipo, sucursal y combinación de ambos |
| **Service Layer** | Interfaz `KpiService` + implementación `KpiServiceImpl`; desacopla la lógica de negocio del controlador |
| **Global Exception Handler** | `@RestControllerAdvice` en `GlobalExceptionHandler`; devuelve errores estandarizados con timestamp |
| **Dependency Injection** | Inyección por constructor vía `@RequiredArgsConstructor` de Lombok |
| **Multi-stage Docker Build** | Imagen de construcción (JDK) separada de la imagen de runtime (JRE) para reducir el tamaño final |
