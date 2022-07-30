package com.example.sistemanominas.repository;

import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.repository.jpa.FormatoArchivoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FormatoArchivoRepositoryImpl {

    @Autowired
    private FormatoArchivoJpa jpaFormatoArchivo;

    public List<FormatoArchivo> lista(String estado){
        return this.jpaFormatoArchivo.findAllByEstado(estado);
    }

    public List<FormatoArchivo> lista(){
        return this.jpaFormatoArchivo.findAll();
    }

    public Optional<FormatoArchivo> guardar(FormatoArchivo f){
        return Optional.of(this.jpaFormatoArchivo.save(f));
    }

    public Optional<FormatoArchivo> formatoArchivoPorId(Integer id){
        return this.jpaFormatoArchivo.findById(id);
    }
}
