package com.example.sistemanominas.repository.jpa;

import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioJpa extends JpaRepository<Usuario, Integer> {
    Usuario findByLogin(String login);

    List<Usuario> findAllByOrderByEstadoAsc();
}
