package com.example.sistemanominas.rest;

import com.example.sistemanominas.service.ParaValServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parametro-valor/api/v1")
public class ParaValRestImpl {

    @Autowired
    private ParaValServiceImpl serviceParaVal;


}
