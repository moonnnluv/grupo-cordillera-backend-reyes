package com.grupcordillera.mskpi.service.impl;

import com.grupcordillera.mskpi.entity.Kpi;
import com.grupcordillera.mskpi.factory.KpiCalculator;
import com.grupcordillera.mskpi.factory.KpiCalculatorFactory;
import com.grupcordillera.mskpi.repository.KpiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KpiServiceImplTest {

    @Mock
    private KpiRepository repository;

    @Mock
    private KpiCalculatorFactory calculatorFactory;

    @InjectMocks
    private KpiServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_retornaListaCompleta() {
        // Arrange
        Kpi kpi1 = new Kpi(1L, "VENTAS", "tasa_conversion", 15.5, "%", LocalDate.now(), "Santiago");
        Kpi kpi2 = new Kpi(2L, "LOGISTICA", "tiempo_entrega", 2.3, "dias", LocalDate.now(), "Valparaiso");
        when(repository.findAll()).thenReturn(Arrays.asList(kpi1, kpi2));

        // Act
        List<Kpi> result = service.listarTodos();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void guardar_persisteKpiYLoRetorna() {
        // Arrange
        Kpi kpi = new Kpi(null, "VENTAS", "tasa_conversion", 15.5, "%", LocalDate.now(), "Santiago");
        Kpi saved = new Kpi(1L, "VENTAS", "tasa_conversion", 15.5, "%", LocalDate.now(), "Santiago");
        when(repository.save(kpi)).thenReturn(saved);

        // Act
        Kpi result = service.guardar(kpi);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("VENTAS", result.getTipo());
        verify(repository).save(kpi);
    }

    @Test
    void listarPorTipo_retornaSoloCoincidencias() {
        // Arrange
        Kpi kpi = new Kpi(1L, "VENTAS", "tasa_conversion", 15.5, "%", LocalDate.now(), "Santiago");
        when(repository.findByTipo("VENTAS")).thenReturn(List.of(kpi));

        // Act
        List<Kpi> result = service.listarPorTipo("VENTAS");

        // Assert
        assertEquals(1, result.size());
        assertEquals("VENTAS", result.get(0).getTipo());
        verify(repository).findByTipo("VENTAS");
    }

    @Test
    void listarPorSucursal_retornaSoloCoincidencias() {
        // Arrange
        Kpi kpi = new Kpi(1L, "VENTAS", "tasa_conversion", 15.5, "%", LocalDate.now(), "Santiago");
        when(repository.findBySucursal("Santiago")).thenReturn(List.of(kpi));

        // Act
        List<Kpi> result = service.listarPorSucursal("Santiago");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Santiago", result.get(0).getSucursal());
        verify(repository).findBySucursal("Santiago");
    }

    @Test
    void listarPorTipoYSucursal_retornaSoloCoincidencias() {
        // Arrange
        Kpi kpi = new Kpi(1L, "VENTAS", "tasa_conversion", 15.5, "%", LocalDate.now(), "Santiago");
        when(repository.findByTipoAndSucursal("VENTAS", "Santiago")).thenReturn(List.of(kpi));

        // Act
        List<Kpi> result = service.listarPorTipoYSucursal("VENTAS", "Santiago");

        // Assert
        assertEquals(1, result.size());
        verify(repository).findByTipoAndSucursal("VENTAS", "Santiago");
    }

    @Test
    void eliminar_invocaDeleteById() {
        // Arrange
        doNothing().when(repository).deleteById(1L);

        // Act
        service.eliminar(1L);

        // Assert
        verify(repository).deleteById(1L);
    }

    @Test
    void calcularYGuardar_usaFactoryYPersiste() {
        // Arrange
        KpiCalculator mockCalculator = mock(KpiCalculator.class);
        when(mockCalculator.calcular(1000.0)).thenReturn(150.0);
        when(mockCalculator.getUnidad()).thenReturn("%");
        when(calculatorFactory.getCalculator("VENTAS")).thenReturn(mockCalculator);

        Kpi saved = new Kpi(1L, "VENTAS", "margen", 150.0, "%", LocalDate.now(), "Santiago");
        when(repository.save(any(Kpi.class))).thenReturn(saved);

        // Act
        Kpi result = service.calcularYGuardar("VENTAS", "margen", 1000.0, "Santiago");

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(150.0, result.getValor());
        verify(calculatorFactory).getCalculator("VENTAS");
        verify(repository).save(any(Kpi.class));
    }

    @Test
    void calcularYGuardar_tipoNoSoportadoLanzaExcepcion() {
        // Arrange
        when(calculatorFactory.getCalculator("DESCONOCIDO"))
                .thenThrow(new IllegalArgumentException("Tipo de KPI no soportado: DESCONOCIDO"));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.calcularYGuardar("DESCONOCIDO", "test", 100.0, "Santiago"));

        assertTrue(ex.getMessage().contains("DESCONOCIDO"));
        verify(calculatorFactory).getCalculator("DESCONOCIDO");
        verify(repository, never()).save(any());
    }

    @Test
    void listarTodos_retornaListaVaciaCuandoNoHayKpis() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Kpi> result = service.listarTodos();

        // Assert
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }
}
