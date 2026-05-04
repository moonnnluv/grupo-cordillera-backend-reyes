package com.grupcordillera.mskpi.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grupcordillera.mskpi.entity.Kpi;
import com.grupcordillera.mskpi.factory.KpiCalculator;
import com.grupcordillera.mskpi.factory.KpiCalculatorFactory;
import com.grupcordillera.mskpi.repository.KpiRepository;
import com.grupcordillera.mskpi.service.KpiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KpiServiceImpl implements KpiService {

    private final KpiRepository repository;
    private final KpiCalculatorFactory calculatorFactory;

    @Override
    public List<Kpi> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Kpi> listarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    @Override
    public List<Kpi> listarPorSucursal(String sucursal) {
        return repository.findBySucursal(sucursal);
    }

    @Override
    public List<Kpi> listarPorTipoYSucursal(String tipo, String sucursal) {
        return repository.findByTipoAndSucursal(tipo, sucursal);
    }

    @Override
    public Kpi guardar(Kpi kpi) {
        return repository.save(kpi);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // Factory Method en acción:
    // La fábrica decide qué calculadora usar según el tipo
    @Override
    public Kpi calcularYGuardar(String tipo, String nombre, double valorBase, String sucursal) {
        KpiCalculator calculator = calculatorFactory.getCalculator(tipo);

        Kpi kpi = new Kpi();
        kpi.setTipo(tipo.toUpperCase());
        kpi.setNombre(nombre);
        kpi.setValor(calculator.calcular(valorBase));
        kpi.setUnidad(calculator.getUnidad());
        kpi.setFecha(LocalDate.now());
        kpi.setSucursal(sucursal);

        return repository.save(kpi);
    }
}