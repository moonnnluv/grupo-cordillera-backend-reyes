package com.grupcordillera.mskpi.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalesKpiCalculatorTest {

    private SalesKpiCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SalesKpiCalculator();
    }

    @Test
    void getTipo_retornaVENTAS() {
        assertEquals("VENTAS", calculator.getTipo());
    }

    @Test
    void calcular_100000_retorna35000() {
        // 100000 * 0.35 = 35000.0
        assertEquals(35000.0, calculator.calcular(100000));
    }

    @Test
    void calcular_0_retorna0() {
        assertEquals(0.0, calculator.calcular(0));
    }

    @Test
    void calcular_valorConDecimales_redondeaA2Decimales() {
        // 333 * 0.35 = 116.55
        assertEquals(116.55, calculator.calcular(333));
    }

    @Test
    void getUnidad_retornaPorcentaje() {
        assertEquals("%", calculator.getUnidad());
    }
}
