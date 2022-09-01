package com.example.sistemanominas.request;

import com.example.sistemanominas.model.FormatoArchivo;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ValidarArchivo {
    private MultipartFile file;
    private FormatoArchivo formato;
}
