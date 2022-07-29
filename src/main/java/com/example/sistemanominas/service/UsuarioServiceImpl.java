package com.example.sistemanominas.service;

import com.example.sistemanominas.component.HashComponent;
import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.Rol;
import com.example.sistemanominas.model.Usuario;
import com.example.sistemanominas.repository.UsuarioRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl {

    @Autowired
    private UsuarioRepositoryImpl repository;

    @Autowired
    private HashComponent hash;

    public List<Usuario> listaUsuarios() {
        return this.repository.lista();
    }

    public ObjectDto guardarUsuario(final Usuario u) {
        ObjectDto respuesta;
        if (this.repository.findByLogin(u.getLogin()).isEmpty()) {
            List<Rol> listaPermisos = u.getPermisos();
            u.setPermisos(null);
            u.setClave(this.hash.sha1(u.getClave()));
            Optional<Usuario> usuario = this.repository.guardar(u);
            if (usuario.isPresent()) {
                usuario.get().setPermisos(listaPermisos);
                usuario = this.repository.guardar(usuario.get());
            }
            respuesta = new ObjectDto(Optional.ofNullable(usuario));
        } else {
            respuesta = new ObjectDto("El usuario (" + u.getLogin() + ") ya esta registrado");
        }
        return respuesta;
    }

    public ObjectDto actualizarUsuario(final Usuario u) {
        ObjectDto respuesta = null;
        Optional<Usuario> usuarioActual = this.repository.usuarioPorId(u.getId());
        if (usuarioActual.isPresent()) {
            if (!usuarioActual.get().getLogin().equals(u.getLogin())) {
                respuesta = this.repository.findByLogin(u.getLogin()).isEmpty()
                        ? null : new ObjectDto("El usuario (" + u.getLogin() + ") ya esta registrado");
            }

            if (respuesta == null) {
                if (!usuarioActual.get().getClave().equals(u.getClave())) {
                    u.setClave(this.hash.sha1(u.getClave()));
                }

                respuesta = new ObjectDto(Optional.ofNullable(this.repository.guardar(u)));
            }
        }else{
            respuesta = new ObjectDto("No se pudo actualizar el usuario");
        }
        return respuesta;
    }


    public Optional<Usuario> estadoUsuario(final Integer id) {
        Optional<Usuario> usuario = this.repository.usuarioPorId(id);
        if (usuario.isPresent()) {
            usuario.get().setEstado(usuario.get().isEnabled() ? Usuario.INACTIVO : Usuario.ACTIVO);
            usuario = this.repository.guardar(usuario.get());
        }
        return usuario;
    }
}
