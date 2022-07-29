package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.Registro;
import com.example.sistemanominas.repository.RegistroRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroServiceImpl {

    @Autowired
    private RegistroRepositoryImpl registroRepository;

    public List<Registro> listaUsuarios() {
        return this.registroRepository.lista();
    }

    public ObjectDto guardarRegistro(final Registro r) {
        ObjectDto respuesta = null;

        if (r.getColumna().matches(".*[0-9].*")) {
            respuesta = new ObjectDto("No se permiten números en la columna");
        }

        if (r.getColumna().length() > 2) {
            respuesta = new ObjectDto("Valor incorrecto en la columna");
        }

        if (r.getTipo().equals(Registro.ENCE) || r.getTipo().equals(Registro.ENCD)) {
            r.setColumna(null);
        }

        if (respuesta == null) {
            Optional<Registro> guardar = this.registroRepository.guardar(r);
            if (guardar.isPresent()) {
                respuesta = new ObjectDto(Optional.of(guardar));
            }
        }

        return respuesta;
    }
}
