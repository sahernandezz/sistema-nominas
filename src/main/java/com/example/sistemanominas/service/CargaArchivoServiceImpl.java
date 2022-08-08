package com.example.sistemanominas.service;

import com.example.sistemanominas.dto.ErrorParVal;
import com.example.sistemanominas.dto.ObjectDto;
import com.example.sistemanominas.model.CargaArchivo;
import com.example.sistemanominas.model.ParaVal;
import com.example.sistemanominas.repository.CargaArchivoRepositoryImpl;
import com.example.sistemanominas.repository.ParaValRepositoryImpl;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class CargaArchivoServiceImpl {

    @Autowired
    private CargaArchivoRepositoryImpl cargaArchivoRepository;

    @Autowired
    private ParaValRepositoryImpl paraValRepository;

    private final static char[] LETRAS = "abcdefghijklmnopqrstuvwxyz".toCharArray();


    public ObjectDto validarArchivo(final MultipartFile file, final String nombreUsuario) throws IOException {
        ObjectDto respuesta;
        List<ErrorParVal> listaErrores = new ArrayList<>();
        List<ParaVal> lista_ENCE = this.paraValRepository.lista(ParaVal.ENCE);
        if (!lista_ENCE.isEmpty()) {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            listaErrores.addAll(this.tipo_ENCE(sheet, lista_ENCE));
            Optional<CargaArchivo> guardar = this.guardarValidacion(file.getOriginalFilename(), nombreUsuario, listaErrores.size());
            respuesta = guardar.isPresent()
                    ? listaErrores.isEmpty()
                    ? new ObjectDto("El archivo no tiene inconsistencias")
                    : new ObjectDto(Optional.of(listaErrores))
                    : new ObjectDto(Optional.of(listaErrores), "No se pudo hacer el registro de la validación");
        } else {
            respuesta = new ObjectDto(Optional.of(listaErrores), "No se puede hacer la validación la que no hay parámetros registrados");
        }
        return respuesta;
    }

    private List<ErrorParVal> tipo_ENCE(final XSSFSheet sheet, final List<ParaVal> lista) {
        List<ErrorParVal> listaErrores = new ArrayList<>();
        if (!lista.isEmpty()) {
            lista.forEach(paraVal -> {
                XSSFCell c = sheet.getRow(Integer.parseInt(paraVal.getCelda()) - 1)
                        .getCell(this.obtenerIndiceColumna(paraVal.getColumna().trim().toLowerCase(), LETRAS, 0, 0));
                ErrorParVal error = this.validarCampo(c, paraVal, ParaVal.ENCE);
                if (error != null) {
                    listaErrores.add(error);
                }
            });
        }
        return listaErrores;
    }

    private int obtenerIndiceColumna(final String columna, char[] letras, int n, int letraLen) {
        for (char letra : letras) {
            if (columna.equals(String.valueOf(letra).toLowerCase())) {
                return n;
            } else {
                n++;
            }
        }

        char[] letrasPalabra = new char[letras.length];
        IntStream.range(0, letras.length).forEach(i -> letrasPalabra[i] = (char) (letras[i] + letras[letraLen]));
        return obtenerIndiceColumna(columna, letrasPalabra, n, letraLen + 1);
    }

    private ErrorParVal validarCampo(final XSSFCell x, final ParaVal p, final String tipo) {
        Object respuesta = null;
        switch (p.getTipoDato()) {
            case ParaVal.STRING -> {
                respuesta = this.validarString(x, p.getValorPer());
            }
            case ParaVal.NUMBER -> {
                respuesta = this.validarNumeric(x.getCellType());
            }
            case ParaVal.DATE -> {
                respuesta = this.validarDate(x, p.getValorPer());
            }
            default -> {
                if (x.getCellType().equals(CellType.BLANK)) {
                    respuesta = "El campo esta vació";
                }
            }
        }

        if (respuesta != null) {
            respuesta = this.nuevoErrorParVal(tipo, p, respuesta.toString());
        }
        return (ErrorParVal) respuesta;
    }

    private String validarString(final XSSFCell valor, final String valorPer) {
        String respuesta = null;
        if (!valor.getCellType().equals(CellType.STRING)) {
            respuesta = "El campo debe ser texto";
        } else {
            if (!valor.getStringCellValue().equals(valorPer)) {
                respuesta = "Valor no permitido debe ser (" + valorPer + ")";
            }
        }
        return respuesta;
    }

    private String validarNumeric(final CellType valor) {
        String respuesta = null;
        if (!valor.equals(CellType.NUMERIC)) {
            respuesta = "El campo debe ser un número";
        }
        return respuesta;
    }

    private String validarDate(final XSSFCell valor, String valorPer) {
        String respuesta = null;
        if (!valor.getCellType().equals(CellType.NUMERIC)) {
            respuesta = "El campo debe ser una fecha";
        } else {
            System.out.println(valor.getDateCellValue());
            try {
                new SimpleDateFormat(valorPer).parse(valor.getDateCellValue().toString());
            } catch (ParseException e) {
                respuesta = "El campo debe tener el formato " + valorPer;
            }
        }
        return respuesta;
    }

    private ErrorParVal nuevoErrorParVal(final String tipo, final ParaVal p, final String mensaje) {
        ErrorParVal error = new ErrorParVal();
        error.setColumna(p.getColumna());
        error.setCelda(p.getCelda());
        error.setTipo(tipo);
        error.setMensaje(mensaje);
        return error;
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
