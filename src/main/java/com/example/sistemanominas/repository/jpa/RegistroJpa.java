package com.example.sistemanominas.repository.jpa;


import com.example.sistemanominas.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroJpa extends JpaRepository<Registro, Integer> {
}
