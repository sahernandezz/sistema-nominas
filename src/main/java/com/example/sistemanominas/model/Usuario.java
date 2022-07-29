package com.example.sistemanominas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable, UserDetails {

    @Serial
    private final static long serialVersionUID = 3214123L;

    public final static String ACTIVO = "A";

    public final static String INACTIVO = "I";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre_usuario", nullable = false, length = 80)
    private String nombre;

    @Column(name = "login_usuario", nullable = false, length = 10, unique = true)
    private String login;

    @Column(name = "clave_usuario", nullable = false, length = 256)
    private String clave;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_permisos", joinColumns = @JoinColumn(referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Rol> permisos;

    @Column(name = "estado_usuario", nullable = false, length = 1)
    private String estado = ACTIVO;

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            result = false;
        } else {
            Usuario usuario = (Usuario) o;
            result = id != null && Objects.equals(id, usuario.id);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permisos;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.clave;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.login;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return this.isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return this.isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return this.isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.estado.equals(ACTIVO);
    }
}
