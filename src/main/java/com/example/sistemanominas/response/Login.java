package com.example.sistemanominas.response;

import com.example.sistemanominas.model.Usuario;
import lombok.Data;

@Data
public class Login {
    private Usuario usuario;
    private String token;

    public Login(Usuario usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }
}
