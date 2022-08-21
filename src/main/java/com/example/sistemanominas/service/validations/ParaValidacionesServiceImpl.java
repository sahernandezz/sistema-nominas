package com.example.sistemanominas.service.validations;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.ParaVal;
import org.springframework.stereotype.Service;

@Service
public class ParaValidacionesServiceImpl {

    public ObjectDto validacionesGuardarParaVal(final ParaVal r) {
        String respuesta = null;

        if (r.getTipo().equals(ParaVal.ENCE) || r.getTipo().equals(ParaVal.ENCD)) {
            if (r.getCelda().isEmpty()) {
                respuesta = "El campo celda no puede estar vació";
            }
        }

        try {
            Integer.parseInt(r.getCelda());
        } catch (NumberFormatException e) {
            respuesta = "Solo se permiten números en el campo celda";
        }
        if (r.getColumna().matches("-?\\d+(\\.\\d+)?")) {
            respuesta = "No se permiten números en el campo columna";
        } else if (r.getColumna().length() > 2) {
            respuesta = "Valor incorrecto en el campo columna";
        } else if (r.getCelda().length() > 5) {
            respuesta = "Valor incorrecto en el campo celda";
        } else if (r.getCelda().equals("0")) {
            respuesta = "El 0 no es un valor valido en el campo celda";
        }

        return respuesta != null ? new ObjectDto(respuesta) : null;
    }
}
