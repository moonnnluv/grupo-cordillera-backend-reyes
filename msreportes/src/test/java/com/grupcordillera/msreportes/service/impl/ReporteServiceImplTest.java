package com.grupcordillera.msreportes.service.impl;

import com.grupcordillera.msreportes.entity.Reporte;
import com.grupcordillera.msreportes.repository.ReporteRepository;
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
import static org.mockito.Mockito.*;

class ReporteServiceImplTest {

    @Mock
    private ReporteRepository repository;

    @InjectMocks
    private ReporteServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_retornaListaCompleta() {
        // Arrange
        Reporte r1 = new Reporte(1L, "Reporte ventas enero", "VENTAS", "Resumen enero", LocalDate.now(), "Santiago");
        Reporte r2 = new Reporte(2L, "Reporte inventario", "INVENTARIO", "Stock total", LocalDate.now(), "Valparaiso");
        when(repository.findAll()).thenReturn(Arrays.asList(r1, r2));

        // Act
        List<Reporte> result = service.listarTodos();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void guardar_persisteReporteYLoRetorna() {
        // Arrange
        Reporte reporte = new Reporte(null, "Reporte ventas", "VENTAS", "Contenido", LocalDate.now(), "Santiago");
        Reporte saved = new Reporte(1L, "Reporte ventas", "VENTAS", "Contenido", LocalDate.now(), "Santiago");
        when(repository.save(reporte)).thenReturn(saved);

        // Act
        Reporte result = service.guardar(reporte);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Reporte ventas", result.getTitulo());
        verify(repository).save(reporte);
    }

    @Test
    void listarPorTipo_retornaSoloCoincidencias() {
        // Arrange
        Reporte r = new Reporte(1L, "Reporte ventas", "VENTAS", "Contenido", LocalDate.now(), "Santiago");
        when(repository.findByTipo("VENTAS")).thenReturn(List.of(r));

        // Act
        List<Reporte> result = service.listarPorTipo("VENTAS");

        // Assert
        assertEquals(1, result.size());
        assertEquals("VENTAS", result.get(0).getTipo());
        verify(repository).findByTipo("VENTAS");
    }

    @Test
    void listarPorSucursal_retornaSoloCoincidencias() {
        // Arrange
        Reporte r = new Reporte(1L, "Reporte ventas", "VENTAS", "Contenido", LocalDate.now(), "Santiago");
        when(repository.findBySucursal("Santiago")).thenReturn(List.of(r));

        // Act
        List<Reporte> result = service.listarPorSucursal("Santiago");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Santiago", result.get(0).getSucursal());
        verify(repository).findBySucursal("Santiago");
    }

    @Test
    void listarPorTipoYSucursal_retornaSoloCoincidencias() {
        // Arrange
        Reporte r = new Reporte(1L, "Reporte ventas", "VENTAS", "Contenido", LocalDate.now(), "Santiago");
        when(repository.findByTipoAndSucursal("VENTAS", "Santiago")).thenReturn(List.of(r));

        // Act
        List<Reporte> result = service.listarPorTipoYSucursal("VENTAS", "Santiago");

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
    void listarPorTipo_retornaListaVaciaParaTipoInexistente() {
        // Arrange
        when(repository.findByTipo("INEXISTENTE")).thenReturn(Collections.emptyList());

        // Act
        List<Reporte> result = service.listarPorTipo("INEXISTENTE");

        // Assert
        assertTrue(result.isEmpty());
        verify(repository).findByTipo("INEXISTENTE");
    }

    @Test
    void listarTodos_retornaListaVaciaCuandoNoHayReportes() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Reporte> result = service.listarTodos();

        // Assert
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }
}
