package com.grupcordillera.msdatos.controller;

import com.grupcordillera.msdatos.entity.DatoOrganizacional;
import com.grupcordillera.msdatos.service.DatoOrganizacionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datos")
@RequiredArgsConstructor
public class DatoOrganizacionalController {

    private final DatoOrganizacionalService service;

    @GetMapping
    public ResponseEntity<List<DatoOrganizacional>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/fuente/{fuente}")
    public ResponseEntity<List<DatoOrganizacional>> listarPorFuente(@PathVariable String fuente) {
        return ResponseEntity.ok(service.listarPorFuente(fuente));
    }

    @GetMapping("/sucursal/{sucursal}")
    public ResponseEntity<List<DatoOrganizacional>> listarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursal));
    }

    @PostMapping
    public ResponseEntity<DatoOrganizacional> guardar(@RequestBody DatoOrganizacional dato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}