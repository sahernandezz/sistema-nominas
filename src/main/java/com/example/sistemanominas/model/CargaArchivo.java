package com.example.sistemanominas.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "carga_archivo")
public class CargaArchivo implements Serializable {

    @Serial
    private final static long serialVersionUID = 34114123L;

    public final static String ACTIVO = "A";

    public final static String INACTIVO = "I";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;





}
