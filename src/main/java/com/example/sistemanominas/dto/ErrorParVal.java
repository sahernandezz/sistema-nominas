package com.example.sistemanominas.dto;

import lombok.Data;

@Data
public class ErrorParVal {
    private String mensaje;
    private String celda;
    private String columna;
    private String tipo;
    private String formato;
}
