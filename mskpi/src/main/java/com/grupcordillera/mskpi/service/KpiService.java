package com.grupcordillera.mskpi.service;

import java.util.List;

import com.grupcordillera.mskpi.entity.Kpi;

public interface KpiService {

    List<Kpi> listarTodos();

    List<Kpi> listarPorTipo(String tipo);

    List<Kpi> listarPorSucursal(String sucursal);

    List<Kpi> listarPorTipoYSucursal(String tipo, String sucursal);

    Kpi guardar(Kpi kpi);

    void eliminar(Long id);
}