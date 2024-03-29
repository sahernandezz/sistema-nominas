package com.example.sistemanominas.rest;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.service.ParaValServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/parametro-valor/api/v1")
public class ParaValRestImpl {

    @Autowired
    private ParaValServiceImpl paraValService;

    @GetMapping("/lista")
    public ResponseEntity<?> listaParaVal() {
        ResponseEntity<?> respuesta;
        try {
            List<ParaVal> lista = this.paraValService.listaParaVal();
            respuesta = lista.isEmpty() ? new ResponseEntity<>(lista, HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearParVal(@RequestBody final ParaVal paraVal) {
        ResponseEntity<?> respuesta;
        try {
            ObjectDto guardar = this.paraValService.guardarParaVal(paraVal);
            respuesta = guardar.getObject().isPresent() ? new ResponseEntity<>(guardar, HttpStatus.OK)
                    : new ResponseEntity<>(guardar, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarParVal(@RequestBody final ParaVal paraVal) {
        ResponseEntity<?> respuesta;
        try {
            ObjectDto actualizar = this.paraValService.actualizarParVal(paraVal);
            respuesta = actualizar.getObject().isPresent() ? new ResponseEntity<>(actualizar, HttpStatus.OK)
                    : new ResponseEntity<>(actualizar, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/estado")
    public ResponseEntity<?> estadoParaVal(@RequestBody final Integer id) {
        ResponseEntity<?> respuesta;
        try {
            if (id != null) {
                Optional<ParaVal> paraVal = this.paraValService.estadoParaVal(id);
                respuesta = paraVal.map(value -> new ResponseEntity<>(Map.of("message", "Formato " +
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
