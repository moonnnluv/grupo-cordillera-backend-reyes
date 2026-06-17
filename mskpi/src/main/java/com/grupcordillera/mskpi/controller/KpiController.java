package com.grupcordillera.mskpi.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupcordillera.mskpi.entity.Kpi;
import com.grupcordillera.mskpi.service.KpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kpi")
@RequiredArgsConstructor
@Tag(name = "KPIs", description = "Operaciones sobre indicadores clave de rendimiento (KPI) de Grupo Cordillera")
public class KpiController {

    private final KpiService service;

    @GetMapping
    @Operation(summary = "Listar todos los KPIs")
    public ResponseEntity<List<Kpi>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar KPIs por tipo")
    public ResponseEntity<List<Kpi>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }

    @GetMapping("/sucursal/{sucursal}")
    @Operation(summary = "Listar KPIs por sucursal")
    public ResponseEntity<List<Kpi>> listarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursal));
    }

    @GetMapping("/tipo/{tipo}/sucursal/{sucursal}")
    @Operation(summary = "Listar KPIs por tipo y sucursal")
    public ResponseEntity<List<Kpi>> listarPorTipoYSucursal(
            @PathVariable String tipo,
            @PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorTipoYSucursal(tipo, sucursal));
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo KPI")
    public ResponseEntity<Kpi> guardar(@RequestBody @Valid Kpi kpi) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(kpi));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un KPI por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calcular")
    @Operation(summary = "Calcular y guardar un KPI a partir de parámetros base")
    public ResponseEntity<Kpi> calcular(
            @RequestParam String tipo,
            @RequestParam String nombre,
            @RequestParam double valorBase,
            @RequestParam(required = false) String sucursal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.calcularYGuardar(tipo, nombre, valorBase, sucursal));
    }
}
