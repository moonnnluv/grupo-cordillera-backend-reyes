package com.grupcordillera.mskpi.factory.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinanceKpiCalculatorTest {

    private FinanceKpiCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FinanceKpiCalculator();
    }

    @Test
    void getTipo_retornaRENTABILIDAD() {
        assertEquals("RENTABILIDAD", calculator.getTipo());
    }

    @Test
    void calcular_100000_retorna115000() {
        // 100000 * 1.15 = 115000.0
        assertEquals(115000.0, calculator.calcular(100000));
    }

    @Test
    void calcular_0_retorna0() {
        assertEquals(0.0, calculator.calcular(0));
    }

    @Test
    void calcular_valorConDecimales_redondeaA2Decimales() {
        // 1000 * 1.15 = 1150.0
        assertEquals(1150.0, calculator.calcular(1000));
    }

    @Test
    void getUnidad_retornaCLP() {
        assertEquals("CLP", calculator.getUnidad());
    }
}
