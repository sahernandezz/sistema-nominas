package com.example.sistemanominas.component;

import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class ExcelMetodosComponent {

    public final static String[] LETRAS = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z".toUpperCase().split(",");

    public int obtenerIndiceColumna(final String columna, String[] letras, int n, int letraLen) {
        for (String letra : letras) {
            if (columna.equals(letra)) {
                return n;
            } else {
                n++;
            }
        }
        return this.obtenerIndiceColumna(columna,
                IntStream.range(0, letras.length).mapToObj(i ->
                        LETRAS[letraLen] + LETRAS[i]
                ).toArray(String[]::new), n, letraLen + 1);
    }
}
