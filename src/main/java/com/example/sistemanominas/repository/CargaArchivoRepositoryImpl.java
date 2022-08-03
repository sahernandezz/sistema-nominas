package com.example.sistemanominas.repository;

import com.example.sistemanominas.model.CargaArchivo;
import com.example.sistemanominas.repository.jpa.CargaArchivoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CargaArchivoRepositoryImpl {

    @Autowired
    private CargaArchivoJpa jpaCargaArchivo;

    public List<CargaArchivo> lista(){
        return this.jpaCargaArchivo.findAll();
    }

    public Optional<CargaArchivo> guardar(final CargaArchivo c){
        return Optional.of(this.jpaCargaArchivo.save(c));
    }

    public Optional<CargaArchivo> cargaArchivoPorId(final Integer id){
        return this.jpaCargaArchivo.findById(id);
    }

}
