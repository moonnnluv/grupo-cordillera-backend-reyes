package com.grupcordillera.mskpi.controller;

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

import com.grupcordillera.mskpi.entity.Kpi;
import com.grupcordillera.mskpi.service.KpiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kpi")
@RequiredArgsConstructor
public class KpiController {

    private final KpiService service;

    @GetMapping
    public ResponseEntity<List<Kpi>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Kpi>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }

    @GetMapping("/sucursal/{sucursal}")
    public ResponseEntity<List<Kpi>> listarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursal));
    }

    @GetMapping("/tipo/{tipo}/sucursal/{sucursal}")
    public ResponseEntity<List<Kpi>> listarPorTipoYSucursal(
            @PathVariable String tipo,
            @PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorTipoYSucursal(tipo, sucursal));
    }

    @PostMapping
    public ResponseEntity<Kpi> guardar(@RequestBody Kpi kpi) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(kpi));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}