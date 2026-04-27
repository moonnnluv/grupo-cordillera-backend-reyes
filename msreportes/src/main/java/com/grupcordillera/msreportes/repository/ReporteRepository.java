package com.grupcordillera.msreportes.repository;

import com.grupcordillera.msreportes.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByTipo(String tipo);

    List<Reporte> findBySucursal(String sucursal);

    List<Reporte> findByTipoAndSucursal(String tipo, String sucursal);
}