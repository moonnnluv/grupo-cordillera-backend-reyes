package com.grupcordillera.msdatos.controller;

import com.grupcordillera.msdatos.entity.DatoOrganizacional;
import com.grupcordillera.msdatos.service.DatoOrganizacionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datos")
@Tag(name = "Datos Organizacionales", description = "Operaciones sobre datos organizacionales de Grupo Cordillera")
public class DatoOrganizacionalController {

    private final DatoOrganizacionalService service;

    public DatoOrganizacionalController(DatoOrganizacionalService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los datos organizacionales")
    public ResponseEntity<List<DatoOrganizacional>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/fuente/{fuente}")
    @Operation(summary = "Listar datos organizacionales por fuente")
    public ResponseEntity<List<DatoOrganizacional>> listarPorFuente(@PathVariable String fuente) {
        return ResponseEntity.ok(service.listarPorFuente(fuente));
    }

    @GetMapping("/sucursal/{sucursal}")
    @Operation(summary = "Listar datos organizacionales por sucursal")
    public ResponseEntity<List<DatoOrganizacional>> listarPorSucursal(@PathVariable String sucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(sucursal));
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo dato organizacional")
    public ResponseEntity<DatoOrganizacional> guardar(@RequestBody @Valid DatoOrganizacional dato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dato));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un dato organizacional por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
