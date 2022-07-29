package com.example.sistemanominas.service;

import com.example.sistemanominas.model.Rol;
import com.example.sistemanominas.repository.RolRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl {

    @Autowired
    private RolRepositoryImpl rolRepository;

    public List<Rol> listaPermisos() {
        return this.rolRepository.lista();
    }
}
