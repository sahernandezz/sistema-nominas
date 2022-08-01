package com.example.sistemanominas.rest;

import com.example.sistemanominas.model.Rol;
import com.example.sistemanominas.service.RolServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permisos/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PermisosRestImpl {

    @Autowired
    private RolServiceImpl rolService;

    @GetMapping("/lista")
    public ResponseEntity<?> listaPermisos() {
        ResponseEntity<?> respuesta;
        try {
            List<Rol> lista = this.rolService.listaPermisos();
            respuesta = lista.isEmpty() ? new ResponseEntity<>(lista,HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(lista,HttpStatus.OK);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

}
