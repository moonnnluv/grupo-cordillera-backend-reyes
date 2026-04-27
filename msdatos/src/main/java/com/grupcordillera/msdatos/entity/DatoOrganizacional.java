package com.grupcordillera.msdatos.entity;

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
@Table(name = "dato_organizacional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatoOrganizacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Origen del dato: "VENTAS", "INVENTARIO", "FINANZAS", "ECOMMERCE"
    @Column(nullable = false)
    private String fuente;

    // Nombre del indicador: "ingresos_mes", "stock_producto_X", etc.
    @Column(nullable = false)
    private String indicador;

    // Valor numérico del indicador
    @Column(nullable = false)
    private Double valor;

    // Fecha del registro
    @Column(nullable = false)
    private LocalDate fecha;

    // Sucursal de origen
    private String sucursal;
}