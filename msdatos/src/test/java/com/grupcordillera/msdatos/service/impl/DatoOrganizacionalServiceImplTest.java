package com.grupcordillera.msdatos.service.impl;

import com.grupcordillera.msdatos.entity.DatoOrganizacional;
import com.grupcordillera.msdatos.repository.DatoOrganizacionalRepository;
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

class DatoOrganizacionalServiceImplTest {

    @Mock
    private DatoOrganizacionalRepository repository;

    @InjectMocks
    private DatoOrganizacionalServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_retornaListaCompleta() {
        // Arrange
        DatoOrganizacional dato1 = new DatoOrganizacional(1L, "VENTAS", "ingresos_mes", 5000.0, LocalDate.now(), "Santiago");
        DatoOrganizacional dato2 = new DatoOrganizacional(2L, "INVENTARIO", "stock_total", 200.0, LocalDate.now(), "Valparaiso");
        when(repository.findAll()).thenReturn(Arrays.asList(dato1, dato2));

        // Act
        List<DatoOrganizacional> result = service.listarTodos();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void guardar_persisteDatoYLoRetorna() {
        // Arrange
        DatoOrganizacional dato = new DatoOrganizacional(null, "FINANZAS", "utilidad_neta", 12000.0, LocalDate.now(), "Santiago");
        DatoOrganizacional saved = new DatoOrganizacional(1L, "FINANZAS", "utilidad_neta", 12000.0, LocalDate.now(), "Santiago");
        when(repository.save(dato)).thenReturn(saved);

        // Act
        DatoOrganizacional result = service.guardar(dato);

        // Assert
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals("FINANZAS", result.getFuente());
        verify(repository).save(dato);
    }

    @Test
    void listarPorFuente_retornaSoloCoincidencias() {
        // Arrange
        DatoOrganizacional dato = new DatoOrganizacional(1L, "VENTAS", "ingresos_mes", 5000.0, LocalDate.now(), "Santiago");
        when(repository.findByFuente("VENTAS")).thenReturn(List.of(dato));

        // Act
        List<DatoOrganizacional> result = service.listarPorFuente("VENTAS");

        // Assert
        assertEquals(1, result.size());
        assertEquals("VENTAS", result.get(0).getFuente());
        verify(repository).findByFuente("VENTAS");
    }

    @Test
    void listarPorSucursal_retornaSoloCoincidencias() {
        // Arrange
        DatoOrganizacional dato = new DatoOrganizacional(1L, "VENTAS", "ingresos_mes", 5000.0, LocalDate.now(), "Santiago");
        when(repository.findBySucursal("Santiago")).thenReturn(List.of(dato));

        // Act
        List<DatoOrganizacional> result = service.listarPorSucursal("Santiago");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Santiago", result.get(0).getSucursal());
        verify(repository).findBySucursal("Santiago");
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
    void listarPorFuente_retornaListaVaciaParaFuenteInexistente() {
        // Arrange
        when(repository.findByFuente("INEXISTENTE")).thenReturn(Collections.emptyList());

        // Act
        List<DatoOrganizacional> result = service.listarPorFuente("INEXISTENTE");

        // Assert
        assertTrue(result.isEmpty());
        verify(repository).findByFuente("INEXISTENTE");
    }

    @Test
    void listarTodos_retornaListaVaciaCuandoNoHayDatos() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<DatoOrganizacional> result = service.listarTodos();

        // Assert
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }
}
