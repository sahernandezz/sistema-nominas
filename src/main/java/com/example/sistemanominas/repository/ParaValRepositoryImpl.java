package com.example.sistemanominas.repository;

import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.jpa.ParaValJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParaValRepositoryImpl {

    @Autowired
    private ParaValJpa jpa;

    public Optional<ParaVal> guardar(ParaVal paraVal) {
        return Optional.of(this.jpa.save(paraVal));
    }

    public List<ParaVal> lista() {
        return this.jpa.findAll();
    }

    public Optional<ParaVal> paraValPorId(final Integer id){
        return this.jpa.findById(id);
    }

    public List<ParaVal> lista(final String tipo) {
        return this.jpa.findAllByTipoOrderById(tipo);
    }
}
