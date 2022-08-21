package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.ParaValRepositoryImpl;
import com.example.sistemanominas.service.validations.ParaValidacionesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ParaValServiceImpl {

    @Autowired
    private ParaValRepositoryImpl paraValRepository;

    @Autowired
    private ParaValidacionesServiceImpl validacionesParVal;

    public List<ParaVal> listaParaVal() {
        return this.paraValRepository.lista();
    }

    public ObjectDto guardarParaVal(final ParaVal r) {
        ObjectDto respuesta = this.validacionesParVal.validacionesGuardarParaVal(r);

        if (respuesta == null) {
            if (!r.getColumna().isEmpty()) {
                r.setColumna(r.getColumna().toUpperCase().trim());
            }

            if (r.getValorPer() != null) {
                r.setValorPer(r.getValorPer().trim());
            }

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

    public Optional<ParaVal> estadoParaVal(final Integer id) {
        Optional<ParaVal> paraVal = this.paraValRepository.paraValPorId(id);
        if (paraVal.isPresent()) {
            paraVal.get().setEstado(paraVal.get().isEnabled() ? ParaVal.INACTIVO : ParaVal.ACTIVO);
            paraVal = this.paraValRepository.guardar(paraVal.get());
        }
        return paraVal;
    }
}
