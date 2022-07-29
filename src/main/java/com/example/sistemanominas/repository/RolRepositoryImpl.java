package com.example.sistemanominas.repository;

import com.example.sistemanominas.model.Rol;
import com.example.sistemanominas.repository.jpa.RolJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RolRepositoryImpl {

    @Autowired
    private RolJpa jpa;

    public List<Rol> lista() {
        return this.jpa.findAll();
    }
}
