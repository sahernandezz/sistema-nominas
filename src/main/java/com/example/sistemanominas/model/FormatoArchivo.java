package com.example.sistemanominas.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "formato_archivo")
public class FormatoArchivo implements Serializable {

    @Serial
    private final static long serialVersionUID = 32423233132L;

    public static final String ACTIVO = "A";

    public final static String INACTIVO = "I";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion", unique = true, nullable = false)
    private String descripcion;

    @Column(name = "estado")
    private String estado = ACTIVO;

    public Boolean isEnabled(){
        return this.estado.equals(ACTIVO);
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            result = false;
        } else {
            FormatoArchivo formatoArchivo = (FormatoArchivo) o;
            result = id != null && Objects.equals(id, formatoArchivo.id);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
