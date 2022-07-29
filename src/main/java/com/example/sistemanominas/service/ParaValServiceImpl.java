package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.ParaValRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParaValServiceImpl {

    @Autowired
    private ParaValRepositoryImpl registroRepository;

    public List<ParaVal> listaUsuarios() {
        return this.registroRepository.lista();
    }

    public ObjectDto guardarRegistro(final ParaVal r) {
        ObjectDto respuesta = null;

        if (r.getColumna().matches(".*[0-9].*")) {
            respuesta = new ObjectDto("No se permiten números en el campo columna");
        }

        if (r.getColumna().length() > 2) {
            respuesta = new ObjectDto("Valor incorrecto en el campo columna");
        }

        if (r.getTipo().equals(ParaVal.ENCE) || r.getTipo().equals(ParaVal.ENCD)) {
            r.setColumna(null);
        }

        if (respuesta == null) {
            Optional<ParaVal> guardar = this.registroRepository.guardar(r);
            if (guardar.isPresent()) {
                respuesta = new ObjectDto(Optional.of(guardar));
            }
        }

        return respuesta;
    }
}
