package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.CargaArchivo;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.repository.CargaArchivoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class CargaArchivoServiceImpl {

    @Autowired
    private CargaArchivoRepositoryImpl cargaArchivoRepository;

    public ObjectDto guardarCargaArchivo(final MultipartFile file, final String nombreUsuario) {
        ObjectDto respuesta;
        CargaArchivo cargaArchivo = new CargaArchivo();
        cargaArchivo.setDescripcion(file.getOriginalFilename());
        cargaArchivo.setLoginUsuario(nombreUsuario);
        cargaArchivo.setFecha(new Date());
        Optional<CargaArchivo> guardar = this.cargaArchivoRepository.guardar(cargaArchivo);
        respuesta = guardar.isPresent() ? new ObjectDto(guardar)
                : new ObjectDto("No se pudo guardar");
        return respuesta;
    }
}
