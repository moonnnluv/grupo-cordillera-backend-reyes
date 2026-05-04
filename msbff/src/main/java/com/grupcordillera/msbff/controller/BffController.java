package com.grupcordillera.msbff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.grupcordillera.msbff.dto.DashboardResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bff")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BffController {

    private final WebClient msDatosClient;
    private final WebClient msKpiClient;
    private final WebClient msReportesClient;

    // Endpoint principal del dashboard — agrega datos de 3 microservicios
    // El frontend hace UNA sola llamada y recibe todo junto
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        // Llama a ms-datos
        List<Object> datos = msDatosClient.get()
                .uri("/api/datos")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();

        // Llama a ms-kpi
        List<Object> kpis = msKpiClient.get()
                .uri("/api/kpi")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();

        // Llama a ms-reportes
        List<Object> reportes = msReportesClient.get()
                .uri("/api/reportes")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();

        return ResponseEntity.ok(
                new DashboardResponse(datos, kpis, reportes, "OK")
        );
    }

    // Endpoint para KPIs filtrados por sucursal
    @GetMapping("/dashboard/sucursal/{sucursal}")
    public ResponseEntity<DashboardResponse> getDashboardPorSucursal(
            @PathVariable String sucursal) {

        List<Object> datos = msDatosClient.get()
                .uri("/api/datos/sucursal/{s}", sucursal)
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();

        List<Object> kpis = msKpiClient.get()
                .uri("/api/kpi/sucursal/{s}", sucursal)
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();

        List<Object> reportes = msReportesClient.get()
                .uri("/api/reportes/sucursal/{s}", sucursal)
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();

        return ResponseEntity.ok(
                new DashboardResponse(datos, kpis, reportes, "OK")
        );
    }
}