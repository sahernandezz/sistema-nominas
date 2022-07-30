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
@Table(name = "para_val")
public class ParaVal implements Serializable {

    @Serial
    private final static long serialVersionUID = 32425333132L;

    public static final String ACTIVO = "A";

    public final static String INACTIVO = "I";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public final static String ENCE = "ENCE";
    public final static String ENCD = "ENCD";

    public final static String DETE = "DETE";

    public final static String DETD = "DETD";

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "celda", length = 5)
    private String celda;

    @Column(name = "columna", length = 2)
    private String columna;

    public final static String NUMBER = "N";

    public final static String DATE = "D";

    public final static String STRING = "S";

    @Column(name = "tipo_dato", length = 1)
    private String tipoDato;

    @Column(name = "valor_permitido", length = 50)
    private String valorPer;

    @Column(name = "expresion_sql", length = 120)
    private String expreSql;

    @Column(name = "estado")
    private String estado = ACTIVO;

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            result = false;
        } else {
            ParaVal paraVal = (ParaVal) o;
            result = id != null && Objects.equals(id, paraVal.id);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
