package com.grupcordillera.msdatos.repository;

import com.grupcordillera.msdatos.entity.DatoOrganizacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatoOrganizacionalRepository extends JpaRepository<DatoOrganizacional, Long> {

    List<DatoOrganizacional> findByFuente(String fuente);

    List<DatoOrganizacional> findBySucursal(String sucursal);
}