package com.grupcordillera.msbff.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.grupcordillera.msbff.dto.DashboardResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/bff")
@CrossOrigin(origins = "http://localhost:5173")
public class BffController {

    private final WebClient msDatosClient;
    private final WebClient msKpiClient;
    private final WebClient msReportesClient;

    // Constructor explícito con @Qualifier para que Spring sepa cuál bean inyectar
    public BffController(
            @Qualifier("msDatosClient") WebClient msDatosClient,
            @Qualifier("msKpiClient") WebClient msKpiClient,
            @Qualifier("msReportesClient") WebClient msReportesClient) {
        this.msDatosClient = msDatosClient;
        this.msKpiClient = msKpiClient;
        this.msReportesClient = msReportesClient;
    }

    @GetMapping("/dashboard")
    @CircuitBreaker(name = "bff-dashboard", fallbackMethod = "dashboardFallback")
    public ResponseEntity<DashboardResponse> getDashboard() {
        List<Object> datos = msDatosClient.get()
                .uri("/api/datos").retrieve()
                .bodyToFlux(Object.class).collectList()
                .onErrorReturn(List.of()).block();

        List<Object> kpis = msKpiClient.get()
                .uri("/api/kpi").retrieve()
                .bodyToFlux(Object.class).collectList()
                .onErrorReturn(List.of()).block();

        List<Object> reportes = msReportesClient.get()
                .uri("/api/reportes").retrieve()
                .bodyToFlux(Object.class).collectList()
                .onErrorReturn(List.of()).block();

        return ResponseEntity.ok(new DashboardResponse(datos, kpis, reportes, "OK"));
    }

    public ResponseEntity<DashboardResponse> dashboardFallback(Exception ex) {
        log.warn("Circuit breaker activado en /bff/dashboard: {}", ex.getMessage());
        return ResponseEntity.ok(
            new DashboardResponse(List.of(), List.of(), List.of(), "SERVICIO_NO_DISPONIBLE")
        );
    }

    @GetMapping("/dashboard/sucursal/{sucursal}")
    @CircuitBreaker(name = "bff-sucursal", fallbackMethod = "sucursalFallback")
    public ResponseEntity<DashboardResponse> getDashboardPorSucursal(@PathVariable String sucursal) {
        List<Object> datos = msDatosClient.get()
                .uri("/api/datos/sucursal/{s}", sucursal).retrieve()
                .bodyToFlux(Object.class).collectList()
                .onErrorReturn(List.of()).block();

        List<Object> kpis = msKpiClient.get()
                .uri("/api/kpi/sucursal/{s}", sucursal).retrieve()
                .bodyToFlux(Object.class).collectList()
                .onErrorReturn(List.of()).block();

        List<Object> reportes = msReportesClient.get()
                .uri("/api/reportes/sucursal/{s}", sucursal).retrieve()
                .bodyToFlux(Object.class).collectList()
                .onErrorReturn(List.of()).block();

        return ResponseEntity.ok(new DashboardResponse(datos, kpis, reportes, "OK"));
    }

    public ResponseEntity<DashboardResponse> sucursalFallback(String sucursal, Exception ex) {
        log.warn("Circuit breaker activado en /bff/sucursal/{}: {}", sucursal, ex.getMessage());
        return ResponseEntity.ok(
            new DashboardResponse(List.of(), List.of(), List.of(), "SERVICIO_NO_DISPONIBLE")
        );
    }
}