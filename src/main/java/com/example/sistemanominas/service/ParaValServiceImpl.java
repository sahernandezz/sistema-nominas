package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.ParaValRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ParaValServiceImpl {

    @Autowired
    private ParaValRepositoryImpl paraValRepository;

    public List<ParaVal> listaParaVal() {
        return this.paraValRepository.lista();
    }

    public ObjectDto guardarParaVal(final ParaVal r) {
        ObjectDto respuesta = this.validacionesGuardarParaVal(r);

        if (!r.getColumna().isEmpty()) {
            r.setColumna(r.getColumna().toUpperCase().trim());
        }

        if (respuesta == null) {
            Optional<ParaVal> guardar = this.paraValRepository.guardar(r);
            respuesta = guardar.isPresent() ? new ObjectDto(guardar)
                    : new ObjectDto("No se pudo guardar");
        }

        return respuesta;
    }

    public ObjectDto actualizarParVal(final ParaVal p) {
        Optional<ParaVal> paraVal = this.paraValRepository.paraValPorId(p.getId());
        return paraVal.isPresent() ? this.guardarParaVal(p) : new ObjectDto("No se pudo actualizar");
    }

    private ObjectDto validacionesGuardarParaVal(final ParaVal r) {
        ObjectDto respuesta = null;

        if (r.getTipo().equals(ParaVal.ENCE) || r.getTipo().equals(ParaVal.ENCD)) {
            if (r.getCelda().isEmpty()) {
                respuesta = new ObjectDto("El campo celda no puede estar vació");
            }
//            if (!r.getColumna().isEmpty()) {
//                respuesta = new ObjectDto("No se permite el ingreso de datos en el campo de la columna");
//            }
        }

        try {
            Integer.parseInt(r.getCelda());
        } catch (NumberFormatException e) {
            respuesta = new ObjectDto("Solo se permiten números en el campo celda");
        }

        if (r.getColumna().matches("-?\\d+(\\.\\d+)?")) {
            respuesta = new ObjectDto("No se permiten números en el campo columna");
        } else if (r.getColumna().length() > 2) {
            respuesta = new ObjectDto("Valor incorrecto en el campo columna");
        } else if (r.getCelda().length() > 5) {
            respuesta = new ObjectDto("Valor incorrecto en el campo celda");
        } else if (r.getCelda().equals("0")) {
            respuesta = new ObjectDto("El 0 no es un valor valido en el campo celda");
        }
        return respuesta;
    }

    public Optional<ParaVal> estadoParaVal(final Integer id) {
        Optional<ParaVal> paraVal = this.paraValRepository.paraValPorId(id);
        if (paraVal.isPresent()) {
            paraVal.get().setEstado(paraVal.get().isEnabled() ? ParaVal.INACTIVO : ParaVal.ACTIVO);
            paraVal = this.paraValRepository.guardar(paraVal.get());
        }
        return paraVal;
    }
}
