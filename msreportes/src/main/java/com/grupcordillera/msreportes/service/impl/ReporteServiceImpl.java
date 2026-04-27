package com.grupcordillera.msreportes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupcordillera.msreportes.entity.Reporte;
import com.grupcordillera.msreportes.repository.ReporteRepository;
import com.grupcordillera.msreportes.service.ReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository repository;

    @Override
    public List<Reporte> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Reporte> listarPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    @Override
    public List<Reporte> listarPorSucursal(String sucursal) {
        return repository.findBySucursal(sucursal);
    }

    @Override
    public List<Reporte> listarPorTipoYSucursal(String tipo, String sucursal) {
        return repository.findByTipoAndSucursal(tipo, sucursal);
    }

    @Override
    public Reporte guardar(Reporte reporte) {
        return repository.save(reporte);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}