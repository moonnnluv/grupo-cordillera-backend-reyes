package com.grupcordillera.msreportes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupcordillera.msreportes.entity.Reporte;
import com.grupcordillera.msreportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Operaciones sobre reportes de Grupo Cordillera")
public class ReporteController {

    private final ReporteService service;

    @GetMapping
    @Operation(summary = "Listar todos los reportes")
    public ResponseEntity<List<Reporte>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar reportes por tipo")
    public ResponseEntity<List<Reporte>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }

    @GetMapping("/sucursal/{sucursal}")
    @Operation(summary = "Listar reportes por sucursal")
    public ResponseEntity<List<Reporte>> listarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursal));
    }

    @GetMapping("/tipo/{tipo}/sucursal/{sucursal}")
    @Operation(summary = "Listar reportes por tipo y sucursal")
    public ResponseEntity<List<Reporte>> listarPorTipoYSucursal(
            @PathVariable String tipo,
            @PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorTipoYSucursal(tipo, sucursal));
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo reporte")
    public ResponseEntity<Reporte> guardar(@RequestBody Reporte reporte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(reporte));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
