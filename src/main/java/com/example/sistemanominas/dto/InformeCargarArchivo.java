package com.example.sistemanominas.dto;

import lombok.Data;

import java.util.List;

@Data
public class InformeCargarArchivo {
    private List<ErrorParVal> listaErrores;
    private int cantidadErrores;
    private int cantidadRegistros;
    private int cantidadRegistrosValidados;
}
