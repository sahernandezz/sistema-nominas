package com.example.sistemanominas.component;

import org.springframework.stereotype.Component;

import static java.util.stream.IntStream.*;

@Component
public class ExcelMetodosComponent {

    public final static String[] LETRAS = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z".toUpperCase().split(",");

    public Integer obtenerIndiceColumna(final String columna) {
        return this.obtenerIndiceColumna(columna.toUpperCase(), LETRAS, 0, 0);
    }

    private Integer obtenerIndiceColumna(final String columna, String[] letras, int n, int letraLen) {
        try {
            for (String letra : letras) {
                if (columna.equals(letra)) {
                    return n;
                } else {
                    n++;
                }
            }
            return this.obtenerIndiceColumna(columna,
                    range(0, letras.length).mapToObj(i -> LETRAS[letraLen] + LETRAS[i]).toArray(String[]::new),
                    n, letraLen + 1);
        } catch (Exception e) {
            return null;
        }
    }
    public String obtenerLetraColumna(final int indice) {
        String respuesta = null;
        try {
            int n = LETRAS.length;
            out:
            for (int i = 0; i < LETRAS.length; i++) {
                if (indice == i) {
                    respuesta = LETRAS[i];
                    break out;
                }
                for (String i2 : LETRAS) {
                    if (indice == n) {
                        respuesta = LETRAS[i] + i2;
                        break out;
                    } else {
                        n++;
                    }
                }
            }
        } catch (Exception e) {
            respuesta = null;
        }
        return respuesta;
    }
}
