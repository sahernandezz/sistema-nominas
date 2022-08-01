package com.example.sistemanominas.rest;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.Usuario;
import com.example.sistemanominas.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuario/api/v1")
@CrossOrigin(origins = "*")
public class UsuarioRestImpl {
    @Autowired
    private UsuarioServiceImpl usuarioService;

    @GetMapping("/lista")
    public ResponseEntity<?> listaDeUsuario() {
        ResponseEntity<?> respuesta;
        try {
            List<Usuario> lista = this.usuarioService.listaUsuarios();
            respuesta = lista.isEmpty() ? new ResponseEntity<>(lista, HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        ResponseEntity<?> respuesta;
        try {
            ObjectDto guardar = this.usuarioService.guardarUsuario(usuario);
            respuesta = guardar.getObject().isPresent() ? new ResponseEntity<>(guardar, HttpStatus.OK)
                    : new ResponseEntity<>(guardar, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Usuario usuario) {
        ResponseEntity<?> respuesta;
        try {
            ObjectDto actualizar = this.usuarioService.actualizarUsuario(usuario);
            respuesta = actualizar.getObject().isPresent() ? new ResponseEntity<>(actualizar, HttpStatus.OK)
                    : new ResponseEntity<>(actualizar, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/estado")
    public ResponseEntity<?> estadoUsuario(@RequestBody Integer id) {
        ResponseEntity<?> respuesta;
        try {
            if (id != null) {
                Optional<Usuario> usuario = this.usuarioService.estadoUsuario(id);
                respuesta = usuario.map(value -> new ResponseEntity<>(Map.of("message", "Usuario " +
                        (value.isEnabled() ? "activo" : "inactivo")), HttpStatus.OK)).orElseGet(() ->
                        new ResponseEntity<>(Map.of("message", "No se pudo actualizar el estado"), HttpStatus.NOT_FOUND));
            } else {
                respuesta = new ResponseEntity<>(Map.of("message", "Datos incorrectos"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }
}
