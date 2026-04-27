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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService service;

    @GetMapping
    public ResponseEntity<List<Reporte>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listarPorTipo(tipo));
    }

    @GetMapping("/sucursal/{sucursal}")
    public ResponseEntity<List<Reporte>> listarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursal));
    }

    @GetMapping("/tipo/{tipo}/sucursal/{sucursal}")
    public ResponseEntity<List<Reporte>> listarPorTipoYSucursal(
            @PathVariable String tipo,
            @PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorTipoYSucursal(tipo, sucursal));
    }

    @PostMapping
    public ResponseEntity<Reporte> guardar(@RequestBody Reporte reporte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(reporte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}