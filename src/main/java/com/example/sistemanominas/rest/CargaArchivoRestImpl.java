package com.example.sistemanominas.rest;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.service.CargaArchivoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/carga-archivo/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CargaArchivoRestImpl {

    @Autowired
    private CargaArchivoServiceImpl cargaArchivoService;

    @PostMapping(value = "/validar")
    public ResponseEntity<?> validarArchivo(@RequestBody final MultipartFile file, final Principal nombreUsuario) {
        ResponseEntity<?> respuesta;
        try {
            if (Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    || Objects.equals(file.getContentType(), "application/vnd.ms-excel")) {
                ObjectDto guardar = this.cargaArchivoService.validarArchivo(file, nombreUsuario.getName());
                respuesta = guardar.getObject().isEmpty() ? new ResponseEntity<>(guardar, HttpStatus.OK)
                        : new ResponseEntity<>(guardar, HttpStatus.BAD_REQUEST);
            } else {
                respuesta = new ResponseEntity<>(Map.of("message", "El formato no esta soportado solo se permiten (xlsx, xls)"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
        e.printStackTrace();
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }
}
