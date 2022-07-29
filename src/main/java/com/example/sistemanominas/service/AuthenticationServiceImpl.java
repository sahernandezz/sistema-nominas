package com.example.sistemanominas.service;


import com.example.sistemanominas.component.HashComponent;
import com.example.sistemanominas.component.JWTTokenHelperComponent;
import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.repository.UsuarioRepositoryImpl;
import com.example.sistemanominas.response.Login;
import com.example.sistemanominas.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl {

    @Autowired
    private UsuarioRepositoryImpl usuarioRepository;

    @Autowired
    private HashComponent hash;

    @Autowired
    private AuthenticationManager authentication;

    @Autowired
    private JWTTokenHelperComponent jWTTokenHelper;

    private final static String INCORRECTO = "usuario o clave incorrectos";

    public ObjectDto loginUsuario(final String login, final String clave) {
        ObjectDto respuesta;
        Optional<Usuario> usuario = this.usuarioRepository.findByLogin(login);
        if (usuario.isPresent()) {
            if (usuario.get().getClave().equals(hash.sha1(clave))) {
                final Authentication auth = this.authentication.authenticate(new UsernamePasswordAuthenticationToken(login, clave));
                SecurityContextHolder.getContext().setAuthentication(auth);
                respuesta = new ObjectDto(Optional.of(new Login(usuario.get(), this.jWTTokenHelper.generateToken(login))),
                        "Bienvenido");
            } else {
                respuesta = new ObjectDto(INCORRECTO);
            }
        } else {
            respuesta = new ObjectDto(INCORRECTO);
        }
        return respuesta;
    }
}
