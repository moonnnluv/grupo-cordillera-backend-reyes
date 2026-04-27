package com.grupcordillera.msdatos.service;

import com.grupcordillera.msdatos.entity.DatoOrganizacional;
import java.util.List;

public interface DatoOrganizacionalService {

    List<DatoOrganizacional> listarTodos();

    List<DatoOrganizacional> listarPorFuente(String fuente);

    List<DatoOrganizacional> listarPorSucursal(String sucursal);

    DatoOrganizacional guardar(DatoOrganizacional dato);

    void eliminar(Long id);
}