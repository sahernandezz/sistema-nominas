package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ErrorParVal;
import com.example.sistemanominas.dto.InformeCargarArchivo;
import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.CargaArchivo;
import com.example.sistemanominas.model.FormatoArchivo;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.CargaArchivoRepositoryImpl;
import com.example.sistemanominas.repository.FormatoArchivoRepositoryImpl;
import com.example.sistemanominas.repository.ParaValRepositoryImpl;
import com.example.sistemanominas.service.validations.CargarArchivoValidacionesServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class CargaArchivoServiceImpl {

    @Autowired
    private CargaArchivoRepositoryImpl cargaArchivoRepository;

    @Autowired
    private FormatoArchivoRepositoryImpl formatoArchivoRepository;

    @Autowired
    private ParaValRepositoryImpl paraValRepository;

    @Autowired
    private CargarArchivoValidacionesServiceImpl validacionesCargarArchivo;


    public ObjectDto validarArchivo(final MultipartFile file, final String nombreUsuario, final Integer id) throws IOException {
        ObjectDto respuesta;
        List<ErrorParVal> listaErrores = new ArrayList<>();
        Optional<FormatoArchivo> formatoArchivo = this.formatoArchivoRepository.formatoArchivoPorId(id);
        if (formatoArchivo.isPresent()) {
            List<ParaVal> lista_ENCE = this.paraValRepository.lista(ParaVal.ENCE, formatoArchivo.get());
            List<ParaVal> lista_ENCD = this.paraValRepository.lista(ParaVal.ENCD, formatoArchivo.get());

            if (lista_ENCE.size() + lista_ENCD.size() == 0) {
                respuesta = new ObjectDto(Optional.of(listaErrores), "No se puede hacer la validación ya que no hay parámetros registrados");
            } else {
                XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                XSSFSheet sheet = workbook.getSheetAt(0);

                listaErrores.addAll(this.validacionesCargarArchivo.validarPorTipo(sheet, lista_ENCE, ParaVal.ENCE, formatoArchivo.get().getDescripcion()));
                listaErrores.addAll(this.validacionesCargarArchivo.validarRegistros(sheet, lista_ENCD, ParaVal.ENCD, formatoArchivo.get().getDescripcion()));

                int totalRegistros = sheet.getPhysicalNumberOfRows() - 6;
                int numeroRegistrosValidos = totalRegistros;

                if (!listaErrores.isEmpty() && !lista_ENCD.isEmpty()) {
                    numeroRegistrosValidos = this.cantidadRegistrosValidos(listaErrores, totalRegistros, ParaVal.ENCD);
                }

                this.guardarValidacion(file.getOriginalFilename(), nombreUsuario, listaErrores.size(), totalRegistros, numeroRegistrosValidos);
                respuesta = listaErrores.isEmpty()
                        ? new ObjectDto("El archivo no tiene inconsistencias")
                        : new ObjectDto(Optional.of(this.informe(listaErrores, totalRegistros, numeroRegistrosValidos)), "Se detectaron inconsistencias en el archivo");
            }
        } else {
            respuesta = new ObjectDto(Optional.of(listaErrores), "No se puede hacer la validación ya que no hay formato registrado");
        }
        return respuesta;
    }

    private Optional<CargaArchivo> guardarValidacion(final String fileName, final String username,
                                                     final int errores, final int registros, final int registrosValidos) {
        CargaArchivo cargaArchivo = new CargaArchivo();
        cargaArchivo.setDescripcion(fileName);
        cargaArchivo.setLoginUsuario(username);
        cargaArchivo.setTRegistros(registros);
        cargaArchivo.setRValidos(registrosValidos);
        cargaArchivo.setContErrores(errores);
        cargaArchivo.setFecha(new Date());
        return this.cargaArchivoRepository.guardar(cargaArchivo);
    }

    private InformeCargarArchivo informe(final List<ErrorParVal> listaErrores, final int cantidadRegistros,
                                         final int cantidadRegistrosValidados) {
        InformeCargarArchivo informe = new InformeCargarArchivo();
        informe.setListaErrores(listaErrores);
        informe.setCantidadErrores(listaErrores.size());
        informe.setCantidadRegistros(cantidadRegistros);
        informe.setCantidadRegistrosValidados(cantidadRegistrosValidados);
        return informe;
    }

    private int cantidadRegistrosValidos(final List<ErrorParVal> listaErrores, final int totalRegistros, final String tipo) {
        int registrosValidos;
        List<ErrorParVal> lista = listaErrores.stream().filter(error -> error.getTipo().equals(tipo)).toList();
        if (!lista.isEmpty()) {
            List<String> count = new ArrayList<>();
            IntStream.range(0, lista.size()).filter(i ->
                    !count.contains(lista.get(i).getCelda())).forEach(i ->
                    count.add(lista.get(i).getCelda()));
            registrosValidos = totalRegistros - count.size();
        } else {
            registrosValidos = totalRegistros;
        }
        return registrosValidos;
    }
}
