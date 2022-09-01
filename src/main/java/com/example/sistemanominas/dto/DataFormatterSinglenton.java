package com.example.sistemanominas.dto;

import org.apache.poi.ss.usermodel.DataFormatter;

public class DataFormatterSinglenton {
    private static DataFormatter singleton = null;

    public static DataFormatter getInstance() {
        if (singleton == null) {
            singleton = new DataFormatter();
        }
        return singleton;
    }

}
