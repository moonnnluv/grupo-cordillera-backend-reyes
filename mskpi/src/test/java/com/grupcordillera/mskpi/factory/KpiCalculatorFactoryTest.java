package com.grupcordillera.mskpi.factory;

import com.grupcordillera.mskpi.factory.impl.FinanceKpiCalculator;
import com.grupcordillera.mskpi.factory.impl.InventoryKpiCalculator;
import com.grupcordillera.mskpi.factory.impl.SalesKpiCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KpiCalculatorFactoryTest {

    private KpiCalculatorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new KpiCalculatorFactory(List.of(
            new SalesKpiCalculator(),
            new FinanceKpiCalculator(),
            new InventoryKpiCalculator()
        ));
    }

    @Test
    void getCalculator_tipoVENTAS_retornaSalesKpiCalculator() {
        KpiCalculator calc = factory.getCalculator("VENTAS");
        assertInstanceOf(SalesKpiCalculator.class, calc);
    }

    @Test
    void getCalculator_tipoRENTABILIDAD_retornaFinanceKpiCalculator() {
        KpiCalculator calc = factory.getCalculator("RENTABILIDAD");
        assertInstanceOf(FinanceKpiCalculator.class, calc);
    }

    @Test
    void getCalculator_tipoINVENTARIO_retornaInventoryKpiCalculator() {
        KpiCalculator calc = factory.getCalculator("INVENTARIO");
        assertInstanceOf(InventoryKpiCalculator.class, calc);
    }

    @Test
    void getCalculator_tipoDesconocido_lanzaIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> factory.getCalculator("DESCONOCIDO")
        );
        assertTrue(ex.getMessage().contains("DESCONOCIDO"));
    }

    @Test
    void getCalculator_tipoEnMinusculas_esNormalizadoYResuelveCalculator() {
        // Factory does tipo.toUpperCase() before lookup
        KpiCalculator calc = factory.getCalculator("ventas");
        assertInstanceOf(SalesKpiCalculator.class, calc);
    }
}
