package com.example.sistemanominas.rest;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.service.ParaValServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
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
                    : new ResponseEntity<>(lista,HttpStatus.OK);
        } catch (Exception e) {
            respuesta = new ResponseEntity<>(Map.of("message", "Ocurrió un error :("), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearParVal(@RequestBody ParaVal paraVal) {
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
}
