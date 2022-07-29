package com.example.sistemanominas.service;

import com.example.sistemanominas.model.Usuario;
import com.example.sistemanominas.repository.UsuarioRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositoryImpl repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Usuario> user = repository.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with userName" + login);
        } else {
            return user.get();
        }
    }
}
