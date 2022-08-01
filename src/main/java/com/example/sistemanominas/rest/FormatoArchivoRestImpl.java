package com.example.sistemanominas.rest;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.service.FormatoArchivoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/formato-archivo/api/v1")
public class FormatoArchivoRestImpl {

    @Autowired
    private FormatoArchivoServiceImpl formatoArchivoService;

    @GetMapping("/lista/activos")
    public ResponseEntity<?> listaFormatoArchivoActivos() {
        ResponseEntity<?> respuesta;
        try {
            List<FormatoArchivo> lista = this.formatoArchivoService.listaFormatoArchivoActivos();
            respuesta = lista.isEmpty() ? new ResponseEntity<>(lista, HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(lista,HttpStatus.OK);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @GetMapping("/lista")
    public ResponseEntity<?> listaFormatoArchivo() {
        ResponseEntity<?> respuesta;
        try {
            List<FormatoArchivo> lista = this.formatoArchivoService.listaFormatoArchivo();
            respuesta = lista.isEmpty() ? new ResponseEntity<>(lista, HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(lista,HttpStatus.OK);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearFormatoArchivo(@RequestBody FormatoArchivo formatoArchivo) {
        ResponseEntity<?> respuesta;
        try {
            System.out.println(formatoArchivo);
            ObjectDto guardar = this.formatoArchivoService.guardarFormatoArchivo(formatoArchivo);
            respuesta = guardar.getObject().isPresent() ? new ResponseEntity<>(guardar, HttpStatus.OK)
                    : new ResponseEntity<>(guardar, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarFormatoArchivo(@RequestBody FormatoArchivo formatoArchivo) {
        ResponseEntity<?> respuesta;
        try {
            ObjectDto actualizar = this.formatoArchivoService.actualizarFormatoArchivo(formatoArchivo);
            respuesta = actualizar.getObject().isPresent() ? new ResponseEntity<>(actualizar, HttpStatus.OK)
                    : new ResponseEntity<>(actualizar, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/estado")
    public ResponseEntity<?> estadoFormatoArchivo(@RequestBody Integer id) {
        ResponseEntity<?> respuesta;
        try {
            if (id != null) {
                Optional<FormatoArchivo> formatoArchivo = this.formatoArchivoService.estadoFormatoArchivo(id);
                respuesta = formatoArchivo.map(value -> new ResponseEntity<>(Map.of("message", "Formato " +
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
