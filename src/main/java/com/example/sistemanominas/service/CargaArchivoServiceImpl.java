package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ErrorParVal;
import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.CargaArchivo;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.CargaArchivoRepositoryImpl;
import com.example.sistemanominas.repository.ParaValRepositoryImpl;
import com.example.sistemanominas.service.validations.CargarArchivoValidacionesServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
public class CargaArchivoServiceImpl {

    @Autowired
    private CargaArchivoRepositoryImpl cargaArchivoRepository;

    @Autowired
    private ParaValRepositoryImpl paraValRepository;

    @Autowired
    private CargarArchivoValidacionesServiceImpl validacionesCargarArchivo;


    public ObjectDto validarArchivo(final MultipartFile file, final String nombreUsuario) throws IOException {
        ObjectDto respuesta;
        List<ErrorParVal> listaErrores = new ArrayList<>();

        List<ParaVal> lista_ENCE = this.paraValRepository.lista(ParaVal.ENCE);

        if (lista_ENCE.size() == 0) {
            respuesta = new ObjectDto(Optional.of(listaErrores), "No se puede hacer la validación ya que no hay parámetros registrados");
        } else {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            listaErrores.addAll(this.validacionesCargarArchivo.validarPorTipo(sheet, lista_ENCE, ParaVal.ENCE));

            this.guardarValidacion(file.getOriginalFilename(), nombreUsuario, listaErrores.size());
            respuesta = listaErrores.isEmpty()
                    ? new ObjectDto("El archivo no tiene inconsistencias")
                    : new ObjectDto(Optional.of(listaErrores), "Se detectaron inconsistencias en el archivo");
        }
        return respuesta;
    }

    private Optional<CargaArchivo> guardarValidacion(final String fileName, final String username, final int errores) {
        CargaArchivo cargaArchivo = new CargaArchivo();
        cargaArchivo.setDescripcion(fileName);
        cargaArchivo.setLoginUsuario(username);
        cargaArchivo.setContErrores(errores);
        cargaArchivo.setFecha(new Date());
        return this.cargaArchivoRepository.guardar(cargaArchivo);
    }
}
