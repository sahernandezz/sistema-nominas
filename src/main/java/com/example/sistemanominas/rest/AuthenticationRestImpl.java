package com.example.sistemanominas.rest;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.request.AuthRequest;
import com.example.sistemanominas.service.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationRestImpl {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        ResponseEntity<?> respuesta;
        try {
            if (!authRequest.getLogin().isEmpty() && !authRequest.getClave().isEmpty()) {
                ObjectDto login = authenticationService.loginUsuario(authRequest.getLogin(), authRequest.getClave());
                respuesta = login.getObject().isPresent() ? new ResponseEntity<>(login, HttpStatus.OK)
                        : new ResponseEntity<>(login, HttpStatus.BAD_REQUEST);
            } else {
                respuesta = new ResponseEntity<>(Map.of("message", "Datos incorrectos"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }
}
