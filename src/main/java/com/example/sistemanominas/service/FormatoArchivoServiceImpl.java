package com.example.sistemanominas.service;

import com.example.sistemanominas.repository.FormatoArchivoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormatoArchivoServiceImpl {

    @Autowired
    private FormatoArchivoRepositoryImpl formatoArchivoRepository;


}
