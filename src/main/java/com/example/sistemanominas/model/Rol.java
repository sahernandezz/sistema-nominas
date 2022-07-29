package com.example.sistemanominas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@JsonIgnoreProperties("authority")
@Table(name = "rol")
public class Rol implements Serializable, GrantedAuthority {

    public static final String ACTIVO = "A";

    @Serial
    private final static long serialVersionUID = 3242123132L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre_rol", length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(name = "estado_rol", nullable = false, length = 1)
    private String estado = ACTIVO;

    @Override
    public String getAuthority() {
        return this.nombre;
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            result = false;
        } else {
            Rol rol = (Rol) o;
            result = id != null && Objects.equals(id, rol.id);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
