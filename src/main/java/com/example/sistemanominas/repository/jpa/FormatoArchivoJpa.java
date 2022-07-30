package com.example.sistemanominas.repository.jpa;

import com.example.sistemanominas.model.FormatoArchivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormatoArchivoJpa extends JpaRepository<FormatoArchivo, Integer> {
    List<FormatoArchivo> findAllByEstado(String estado);
}
