package com.grupcordillera.msbff.circuit;

import com.grupcordillera.msbff.controller.BffController;
import com.grupcordillera.msbff.dto.DashboardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CircuitBreakerTest {

    private BffController controller;

    @BeforeEach
    void setUp() {
        // Instantiate directly with mocked WebClients to test fallback logic in isolation
        controller = new BffController(
            mock(WebClient.class),
            mock(WebClient.class),
            mock(WebClient.class)
        );
    }

    @Test
    void dashboardFallback_retornaEstadoServicioNoDisponible() {
        ResponseEntity<DashboardResponse> response =
            controller.dashboardFallback(new RuntimeException("timeout"));

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("SERVICIO_NO_DISPONIBLE", response.getBody().getEstado());
    }

    @Test
    void dashboardFallback_retornaListasVacias() {
        DashboardResponse body =
            controller.dashboardFallback(new RuntimeException("conexión rechazada")).getBody();

        assertNotNull(body);
        assertTrue(body.getDatos().isEmpty());
        assertTrue(body.getKpis().isEmpty());
        assertTrue(body.getReportes().isEmpty());
    }

    @Test
    void sucursalFallback_retornaEstadoServicioNoDisponible() {
        ResponseEntity<DashboardResponse> response =
            controller.sucursalFallback("SANTIAGO", new RuntimeException("ms-datos caído"));

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("SERVICIO_NO_DISPONIBLE", response.getBody().getEstado());
    }

    @Test
    void sucursalFallback_retornaListasVacias() {
        DashboardResponse body =
            controller.sucursalFallback("VALPARAISO", new IllegalStateException("error")).getBody();

        assertNotNull(body);
        assertTrue(body.getDatos().isEmpty());
        assertTrue(body.getKpis().isEmpty());
        assertTrue(body.getReportes().isEmpty());
    }

    @Test
    void dashboardFallback_cualquierExcepcion_siempreRetornaFallback() {
        ResponseEntity<DashboardResponse> r1 =
            controller.dashboardFallback(new RuntimeException("error 1"));
        ResponseEntity<DashboardResponse> r2 =
            controller.dashboardFallback(new IllegalStateException("error 2"));

        assertEquals("SERVICIO_NO_DISPONIBLE", r1.getBody().getEstado());
        assertEquals("SERVICIO_NO_DISPONIBLE", r2.getBody().getEstado());
    }

    @Test
    void dashboardResponse_respuestaNormal_tieneEstadoOK() {
        DashboardResponse normal = new DashboardResponse(List.of(), List.of(), List.of(), "OK");
        assertEquals("OK", normal.getEstado());
    }
}
