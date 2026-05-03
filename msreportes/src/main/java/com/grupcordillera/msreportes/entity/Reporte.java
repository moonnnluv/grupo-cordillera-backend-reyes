package com.grupcordillera.msreportes.entity;

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
@Table(name = "reporte")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;        // "Reporte mensual ventas"

    @Column(nullable = false)
    private String tipo;          // "VENTAS", "INVENTARIO", "FINANCIERO"

    @Column(nullable = false)
    private String contenido;     // Resumen o descripción del reporte

    @Column(nullable = false)
    private LocalDate fecha;

    private String sucursal;
}