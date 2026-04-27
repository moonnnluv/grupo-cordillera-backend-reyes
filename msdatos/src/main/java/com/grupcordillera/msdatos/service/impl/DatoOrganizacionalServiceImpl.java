package com.grupcordillera.msdatos.service.impl;

import com.grupcordillera.msdatos.repository.DatoOrganizacionalRepository;
import com.grupcordillera.msdatos.entity.DatoOrganizacional;
import com.grupcordillera.msdatos.service.DatoOrganizacionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatoOrganizacionalServiceImpl implements DatoOrganizacionalService {

    private final DatoOrganizacionalRepository repository;

    @Override
    public List<DatoOrganizacional> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<DatoOrganizacional> listarPorFuente(String fuente) {
        return repository.findByFuente(fuente);
    }

    @Override
    public List<DatoOrganizacional> listarPorSucursal(String sucursal) {
        return repository.findBySucursal(sucursal);
    }

    @Override
    public DatoOrganizacional guardar(DatoOrganizacional dato) {
        return repository.save(dato);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}