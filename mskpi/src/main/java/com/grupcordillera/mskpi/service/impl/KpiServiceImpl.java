package com.grupcordillera.mskpi.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupcordillera.mskpi.entity.Kpi;
import com.grupcordillera.mskpi.repository.KpiRepository;
import com.grupcordillera.mskpi.service.KpiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KpiServiceImpl implements KpiService {

    private final KpiRepository repository;

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
}