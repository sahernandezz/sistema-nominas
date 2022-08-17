package com.example.sistemanominas.repository;

import com.example.sistemanominas.model.Usuario;
import com.example.sistemanominas.repository.jpa.UsuarioJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepositoryImpl {

    @Autowired
    private UsuarioJpa jpa;

    public Optional<Usuario> guardar(Usuario usuario) {
        return Optional.of(this.jpa.save(usuario));
    }

    public List<Usuario> lista() {
        return this.jpa.findAllByOrderByEstadoAsc();
    }

    public Optional<Usuario> findByLogin(String login) {
        return Optional.ofNullable(this.jpa.findByLogin(login));
    }

    public Optional<Usuario> usuarioPorId(Integer id) {
        return this.jpa.findById(id);
    }

}
