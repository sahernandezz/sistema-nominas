package com.example.sistemanominas.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "total_registros", nullable = false)
    private Integer tRegistros = 0;

    @Column(name = "total_registros", nullable = false)
    private Integer rValidos = 0;

    @Column(name = "conteo_errores")
    private Integer contErrores = 0;

    @Column(name = "login_usuario", nullable = false, length = 10)
    private String loginUsuario;

    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado = ACTIVO;

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            result = false;
        } else {
            CargaArchivo cargaArchivo = (CargaArchivo) o;
            result = id != null && Objects.equals(id, cargaArchivo.id);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
