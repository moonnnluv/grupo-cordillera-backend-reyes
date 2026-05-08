-- =============================================
-- GRUPO CORDILLERA — Datos iniciales del sistema
-- DSY1106 Desarrollo Fullstack III · DuocUC
-- =============================================

-- ── baseauth ──────────────────────────────────
USE baseauth;

-- Usuarios de prueba (password: Admin123!)
-- Hash BCrypt de "Admin123!" generado con strength 10
INSERT INTO usuarios (username, password, email, rol, enabled) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9.', 'admin@cordillera.cl', 'ADMIN_GENERAL', true),
('jefa.santiago', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9.', 'jefa@santiago.cordillera.cl', 'ADMIN_SUCURSAL', true),
('vendedor1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9.', 'vendedor1@cordillera.cl', 'VENDEDOR', true),
('vendedor2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9.', 'vendedor2@cordillera.cl', 'VENDEDOR', true);

-- ── baseorganizacional ────────────────────────
USE baseorganizacional;

INSERT INTO dato_organizacional (fuente, indicador, valor, fecha, sucursal) VALUES
('VENTAS',     'ingresos_mes_abril',     125000000, '2025-04-30', 'SANTIAGO'),
('VENTAS',     'ingresos_mes_abril',      87500000, '2025-04-30', 'VALPARAISO'),
('VENTAS',     'ingresos_mes_abril',      63000000, '2025-04-30', 'CONCEPCION'),
('ECOMMERCE',  'ventas_online_abril',     42000000, '2025-04-30', 'SANTIAGO'),
('ECOMMERCE',  'ventas_online_abril',     18500000, '2025-04-30', 'VALPARAISO'),
('INVENTARIO', 'stock_electronica',           4250, '2025-04-30', 'SANTIAGO'),
('INVENTARIO', 'stock_electronica',           2100, '2025-04-30', 'VALPARAISO'),
('INVENTARIO', 'stock_hogar',                 8700, '2025-04-30', 'SANTIAGO'),
('INVENTARIO', 'stock_hogar',                 5300, '2025-04-30', 'CONCEPCION'),
('FINANZAS',   'costo_operacional_abril', 38000000, '2025-04-30', 'SANTIAGO'),
('FINANZAS',   'costo_operacional_abril', 24000000, '2025-04-30', 'VALPARAISO'),
('FINANZAS',   'utilidad_bruta_abril',    87000000, '2025-04-30', 'SANTIAGO'),
('VENTAS',     'ingresos_mes_marzo',     118000000, '2025-03-31', 'SANTIAGO'),
('VENTAS',     'ingresos_mes_marzo',      81000000, '2025-03-31', 'VALPARAISO');

-- ── basekpi ───────────────────────────────────
USE basekpi;

INSERT INTO kpi (tipo, nombre, valor, unidad, fecha, sucursal) VALUES
('VENTAS',       'margen_ventas_abril_stgo',    43750000, '%',          '2025-04-30', 'SANTIAGO'),
('VENTAS',       'margen_ventas_abril_valpo',   30625000, '%',          '2025-04-30', 'VALPARAISO'),
('VENTAS',       'margen_ventas_abril_conce',   22050000, '%',          '2025-04-30', 'CONCEPCION'),
('INVENTARIO',   'rotacion_diaria_electronica',    141.67, 'unidades/día', '2025-04-30', 'SANTIAGO'),
('INVENTARIO',   'rotacion_diaria_hogar',           290,  'unidades/día', '2025-04-30', 'SANTIAGO'),
('RENTABILIDAD', 'rentabilidad_abril_stgo',    100050000, 'CLP',        '2025-04-30', 'SANTIAGO'),
('RENTABILIDAD', 'rentabilidad_abril_valpo',    70587500, 'CLP',        '2025-04-30', 'VALPARAISO'),
('VENTAS',       'margen_ventas_marzo_stgo',    41300000, '%',          '2025-03-31', 'SANTIAGO');

-- ── basereportes ──────────────────────────────
USE basereportes;

INSERT INTO reporte (titulo, tipo, contenido, fecha, sucursal) VALUES
('Reporte de Ventas Abril 2025 — Santiago',
 'VENTAS',
 'Resumen consolidado de ventas del mes de abril para la sucursal Santiago. Ingresos totales: $125.000.000 CLP. Crecimiento respecto a marzo: 5,9%.',
 '2025-04-30', 'SANTIAGO'),

('Reporte de Ventas Abril 2025 — Valparaíso',
 'VENTAS',
 'Resumen consolidado de ventas del mes de abril para la sucursal Valparaíso. Ingresos totales: $87.500.000 CLP.',
 '2025-04-30', 'VALPARAISO'),

('Reporte de Inventario Abril 2025 — Santiago',
 'INVENTARIO',
 'Estado del inventario al cierre de abril. Stock electrónica: 4.250 unidades. Stock hogar: 8.700 unidades. Sin quiebres de stock detectados.',
 '2025-04-30', 'SANTIAGO'),

('Reporte Financiero Q1 2025',
 'FINANCIERO',
 'Análisis financiero del primer trimestre 2025. Utilidad bruta consolidada: $87.000.000 CLP. Margen operacional promedio: 38,2%.',
 '2025-03-31', NULL),

('Reporte de Ventas Online Abril 2025',
 'VENTAS',
 'Desempeño del canal ecommerce en abril. Ventas online Santiago: $42.000.000. Ventas online Valparaíso: $18.500.000. Participación online: 22% del total.',
 '2025-04-30', NULL),

('Reporte KPIs Estratégicos — Abril 2025',
 'FINANCIERO',
 'Indicadores clave de desempeño del mes de abril. Margen de ventas consolidado: 35%. Rotación de inventario diaria promedio: 210 unidades. Rentabilidad sobre ingresos: 115%.',
 '2025-04-30', NULL);
