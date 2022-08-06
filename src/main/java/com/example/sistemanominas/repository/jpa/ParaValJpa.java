package com.example.sistemanominas.repository.jpa;


import com.example.sistemanominas.model.ParaVal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParaValJpa extends JpaRepository<ParaVal, Integer> {
    public List<ParaVal> findAllByTipoOrderById(String tipo);
}
