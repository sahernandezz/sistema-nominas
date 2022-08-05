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

@RestController
@RequestMapping("/carga-archivo/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CargaArchivoRestImpl {

    @Autowired
    private CargaArchivoServiceImpl cargaArchivoService;

    @PostMapping(value = "/validar")
    public ResponseEntity<?> guardarCargaArchivo(@RequestBody final MultipartFile file, final Principal nombreUsuario) {
        ResponseEntity<?> respuesta;
        try {
            if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    || file.getContentType().equals("application/vnd.ms-excel")) {
                ObjectDto guardar = this.cargaArchivoService.guardarCargaArchivo(file, nombreUsuario.getName());
                respuesta = guardar.getObject().isPresent() ? new ResponseEntity<>(Map.of("message", "Archivo cargado correctamente"), HttpStatus.OK)
                        : new ResponseEntity<>(guardar, HttpStatus.BAD_REQUEST);
            } else {
                respuesta = new ResponseEntity<>(Map.of("message", "El formato no esta soportado solo se permiten (xlsx, xls)"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }
}
