package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.repository.FormatoArchivoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormatoArchivoServiceImpl {

    @Autowired
    private FormatoArchivoRepositoryImpl formatoArchivoRepository;

    public ObjectDto guardarFormatoArchivo(final FormatoArchivo f) {
        ObjectDto respuesta;
        if (this.formatoArchivoRepository.findByDescripcion(f.getDescripcion()).isEmpty()) {
            Optional<FormatoArchivo> guardar = this.formatoArchivoRepository.guardar(f);
            respuesta = guardar.isPresent() ? new ObjectDto(guardar)
                    : new ObjectDto("No se pudo guardar");
        } else {
            respuesta = new ObjectDto("La descripción ya se encuentra registrada");
        }
        return respuesta;
    }

    public ObjectDto actualizarFormatoArchivo(final FormatoArchivo f) {
        Optional<FormatoArchivo> formatoArchivo = this.formatoArchivoRepository.formatoArchivoPorId(f.getId());
        ObjectDto guardar = guardarFormatoArchivo(f);
        return formatoArchivo.isPresent() ? guardar : new ObjectDto("No se pudo actualizar");
    }

    public List<FormatoArchivo> listaFormatoArchivoActivos() {
        return this.formatoArchivoRepository.lista(FormatoArchivo.ACTIVO);
    }

    public List<FormatoArchivo> listaFormatoArchivo() {
        return this.formatoArchivoRepository.lista();
    }

    public Optional<FormatoArchivo> estadoFormatoArchivo(final Integer id) {
        Optional<FormatoArchivo> formatoArchivo = this.formatoArchivoRepository.formatoArchivoPorId(id);
        if (formatoArchivo.isPresent()) {
            formatoArchivo.get().setEstado(formatoArchivo.get().isEnabled() ? FormatoArchivo.INACTIVO : FormatoArchivo.ACTIVO);
            formatoArchivo = this.formatoArchivoRepository.guardar(formatoArchivo.get());
        }
        return formatoArchivo;
    }

}
