package com.grupcordillera.msreportes.service;

import java.util.List;

import com.grupcordillera.msreportes.entity.Reporte;

public interface ReporteService {

    List<Reporte> listarTodos();

    List<Reporte> listarPorTipo(String tipo);

    List<Reporte> listarPorSucursal(String sucursal);

    List<Reporte> listarPorTipoYSucursal(String tipo, String sucursal);

    Reporte guardar(Reporte reporte);

    void eliminar(Long id);
}