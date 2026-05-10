# ms-kpi — Microservicio de Gestión de KPIs
**Grupo Cordillera** · DSY1106 Desarrollo Fullstack III · DuocUC 2026

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
| `valor` | Double | Valor del KPI (resultado calculado y persistido) |
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

```bash
docker compose down      # detener
docker compose down -v   # detener y eliminar volúmenes
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
| `POST` | `/api/kpi` | Crea y guarda un KPI con valor ya calculado |
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

| Patrón | Clase(s) | Justificación |
|---|---|---|
| **Factory Method** | `KpiCalculatorFactory` | Centraliza la selección de la calculadora correcta según el tipo de KPI. El cliente (`KpiService`) no conoce las implementaciones concretas; si se agrega un nuevo tipo de KPI, solo se crea una nueva clase sin modificar el código existente (principio Open/Closed) |
| **Strategy** | `KpiCalculator`, `SalesKpiCalculator`, `FinanceKpiCalculator`, `InventoryKpiCalculator` | Encapsula cada algoritmo de cálculo en su propia clase, permitiendo intercambiarlos en tiempo de ejecución. Cada tipo de KPI tiene su fórmula aislada, facilitando modificaciones sin afectar a los demás |
| **Repository Pattern** | `KpiRepository` | Abstrae el acceso a datos detrás de una interfaz. Si se cambia el motor de persistencia, solo se modifica el repositorio sin impactar la lógica de negocio |
| **Service Layer** | `KpiService` / `KpiServiceImpl` | Desacopla la lógica de negocio del controlador REST. El controlador solo delega; si la lógica de cálculo cambia, el contrato de la API permanece estable |
| **Global Exception Handler** | `GlobalExceptionHandler` | Centraliza el manejo de errores con `@RestControllerAdvice`, evitando duplicación de lógica de error y garantizando respuestas consistentes con timestamp |
| **Dependency Injection** | Constructores + `@RequiredArgsConstructor` | Inyección por constructor garantiza inmutabilidad y facilita pruebas unitarias con mocks |
| **Multi-stage Docker Build** | `Dockerfile` | Separa la fase de compilación (JDK) de la imagen de runtime (JRE), reduciendo el tamaño final de la imagen y la superficie de ataque |
