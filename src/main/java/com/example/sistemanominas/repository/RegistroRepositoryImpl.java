package com.example.sistemanominas.repository;

import com.example.sistemanominas.model.Registro;
import com.example.sistemanominas.repository.jpa.RegistroJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RegistroRepositoryImpl {

    @Autowired
    private RegistroJpa jpa;

    public Optional<Registro> guardar(Registro registro) {
        return Optional.of(this.jpa.save(registro));
    }

    public List<Registro> lista() {
        return this.jpa.findAll();
    }
}
