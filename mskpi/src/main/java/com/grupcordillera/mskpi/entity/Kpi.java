package com.grupcordillera.mskpi.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "kpi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kpi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tipo de KPI: "VENTAS", "LOGISTICA", "RENTABILIDAD", "INVENTARIO"
    @Column(nullable = false)
    private String tipo;

    // Nombre del indicador: "margen_bruto", "tasa_conversion", etc.
    @Column(nullable = false)
    private String nombre;

    // Valor calculado del KPI
    @Column(nullable = false)
    private Double valor;

    // Unidad: "%", "CLP", "unidades", etc.
    @Column(nullable = false)
    private String unidad;

    // Fecha del cálculo
    @Column(nullable = false)
    private LocalDate fecha;

    // Sucursal asociada
    private String sucursal;
}