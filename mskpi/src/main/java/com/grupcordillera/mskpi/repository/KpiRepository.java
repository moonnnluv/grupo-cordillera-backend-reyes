package com.grupcordillera.mskpi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupcordillera.mskpi.entity.Kpi;

@Repository
public interface KpiRepository extends JpaRepository<Kpi, Long> {

    List<Kpi> findByTipo(String tipo);

    List<Kpi> findBySucursal(String sucursal);

    List<Kpi> findByTipoAndSucursal(String tipo, String sucursal);
}