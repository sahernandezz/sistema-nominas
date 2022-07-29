package com.example.sistemanominas;

import com.example.sistemanominas.model.Rol;
import com.example.sistemanominas.model.Usuario;
import com.example.sistemanominas.rest.UsuarioRestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class SistemaNominasApplication {

    @Autowired
    private UsuarioRestImpl rest;

    public static void main(String[] args) {
        SpringApplication.run(SistemaNominasApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*");
            }
        };
    }

    @PostConstruct
    public void init() {
        Usuario usuario = new Usuario();
        usuario.setNombre("admin");
        usuario.setLogin("admin");
        usuario.setClave("admin");
        usuario.setPermisos(List.of(
                new Rol("administrador"),
                new Rol("usuario operativo")
        ));
        System.out.println(this.rest.crearUsuario(usuario));
    }

}
