package com.grupcordillera.mskpi.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryKpiCalculatorTest {

    private InventoryKpiCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new InventoryKpiCalculator();
    }

    @Test
    void getTipo_retornaINVENTARIO() {
        assertEquals("INVENTARIO", calculator.getTipo());
    }

    @Test
    void calcular_4500_retorna150() {
        // Math.round((4500 / 30.0) * 100.0) / 100.0 = 150.0
        assertEquals(150.0, calculator.calcular(4500));
    }

    @Test
    void calcular_100_redondeaA2Decimales() {
        // Math.round((100 / 30.0) * 100.0) / 100.0 = Math.round(333.33) / 100.0 = 3.33
        assertEquals(3.33, calculator.calcular(100));
    }

    @Test
    void calcular_0_retorna0() {
        assertEquals(0.0, calculator.calcular(0));
    }

    @Test
    void getUnidad_retornaUnidadesPorDia() {
        assertEquals("unidades/día", calculator.getUnidad());
    }
}
