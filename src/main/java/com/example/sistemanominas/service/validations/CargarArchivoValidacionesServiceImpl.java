package com.example.sistemanominas.service.validations;

import com.example.sistemanominas.component.ExcelMetodosComponent;
import com.example.sistemanominas.dto.ErrorParVal;
import com.example.sistemanominas.dto.DataFormatterSinglenton;
import com.example.sistemanominas.model.ParaVal;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CargarArchivoValidacionesServiceImpl {

    @Autowired
    private ExcelMetodosComponent componentExcel;

    public List<ErrorParVal> validarPorTipo(final XSSFSheet sheet, final List<ParaVal> lista, final String tipo) {
        List<ErrorParVal> listaErrores = new ArrayList<>();
        if (!lista.isEmpty()) {
            lista.forEach(paraVal -> {
                try {
                    ErrorParVal error = this.validarCampo(
                            sheet.getRow(Integer.parseInt(paraVal.getCelda()) - 1)
                                    .getCell(this.componentExcel.obtenerIndiceColumna(paraVal.getColumna())),
                            paraVal, tipo);

                    if (error != null) {
                        listaErrores.add(error);
                    }
                } catch (Exception e) {
                    listaErrores.add(this.nuevoErrorParVal(
                            tipo, paraVal, "Verifique la información de la validación"));
                }
            });
        }
        return listaErrores;
    }

    public List<ErrorParVal> validarRegistros(final XSSFSheet sheet, final List<ParaVal> lista, final String tipo) {
        List<ErrorParVal> listaErrores = new ArrayList<>();
        if (!lista.isEmpty()) {
            lista.forEach(paraVal -> {
                try {
                    Integer celda = this.componentExcel.obtenerIndiceColumna(paraVal.getColumna());
                    String validarEncabezado = this.validarString(sheet.getRow(5).getCell(celda), paraVal.getValorPer());
                    if (validarEncabezado == null) {
                        IntStream.range(6, sheet.getPhysicalNumberOfRows()).forEach(i -> {
                            ParaVal paraVal2 = paraVal;
                            paraVal2.setCelda(String.valueOf(i + 1));
                            paraVal2.setValorPer(null);
                            ErrorParVal error = this.validarCampo(sheet.getRow(i).getCell(celda), paraVal2, tipo);
                            if (error != null) {
                                listaErrores.add(error);
                            }
                        });
                    } else {
                        listaErrores.add(this.nuevoErrorParVal(tipo, paraVal, validarEncabezado
                                + " Por tanto no se puede hacer La validación de la columna (" + paraVal.getColumna() + ")"));
                    }
                } catch (Exception e) {
                    listaErrores.add(this.nuevoErrorParVal(
                            tipo, paraVal, "Verifique la información de la validación"));
                }
            });
        }
        return listaErrores;
    }

    private ErrorParVal validarCampo(final XSSFCell x, final ParaVal p, final String tipo) {
        Object respuesta = null;
        try {
            switch (p.getTipoDato()) {
                case ParaVal.STRING -> {
                    respuesta = this.validarString(x, p.getValorPer());
                }
                case ParaVal.NUMBER -> {
                    respuesta = this.validarNumeric(x, p.getValorPer());
                }
                case ParaVal.DATE -> {
                    respuesta = this.validarDate(x, p.getValorPer());
                }
            }

            if (respuesta != null) {
                respuesta = this.nuevoErrorParVal(tipo, p, respuesta.toString());
            }
        } catch (Exception e) {
            respuesta = this.nuevoErrorParVal(tipo, p, "Hay problemas al verificar este campo");
        }
        return (ErrorParVal) respuesta;
    }

    private String validarString(final XSSFCell valor, final String valorPer) {
        String respuesta = null;
        String valorPermitido = "Valor no permitido debe ser (" + valorPer + ")";
        if (DataFormatterSinglenton.getInstance().formatCellValue(valor).isEmpty()) {
            respuesta = "El campo esta vació";
        } else {
            if (!valor.getCellType().equals(CellType.STRING)) {
                respuesta = "El valor no es texto";
            } else {
                if (valorPer != null) {
                    if (!valorPer.contains(",")) {
                        if (!valor.getStringCellValue().replace(" ", "").equals(valorPer.replace(" ", ""))) {
                            respuesta = valorPermitido;
                        }
                    } else {
                        if (!Arrays.asList(valorPer.replace(" ", "").split(","))
                                .contains(DataFormatterSinglenton.getInstance().formatCellValue(valor).replace(" ", ""))) {
                            respuesta = valorPermitido;
                        }
                    }
                }
            }
        }

        return respuesta;
    }

    private String validarNumeric(final XSSFCell valor, final String valorPer) {
        String respuesta = null;

        String valorString = DataFormatterSinglenton.getInstance().formatCellValue(valor)
                .replace(" ", "");

        try {
            Double.parseDouble(valorString);
        } catch (NumberFormatException e) {
            respuesta = "El campo debe ser un número";
        }

        if (valorPer != null) {
            if (!valorString.equals(valorPer.replace(" ", ""))) {
                respuesta = "Valor no permitido debe ser (" + valorPer + ")";
            }
        }

        return respuesta;
    }

    private String validarDate(final XSSFCell valor, final String valorPer) {
        String respuesta = null;
        try {
            new SimpleDateFormat(valorPer).parse(DataFormatterSinglenton.getInstance().formatCellValue(valor).replace(" ", ""));
        } catch (ParseException e) {
            respuesta = "El campo debe tener el formato " + valorPer;
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
}
