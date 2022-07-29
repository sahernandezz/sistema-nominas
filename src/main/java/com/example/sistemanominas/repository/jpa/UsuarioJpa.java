package com.example.sistemanominas.repository.jpa;

import com.example.sistemanominas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpa extends JpaRepository<Usuario, Integer> {
    Usuario findByLogin(String login);
}
