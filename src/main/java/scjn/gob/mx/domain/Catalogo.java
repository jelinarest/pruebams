package scjn.gob.mx.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Catalogo.
 */
@Entity
@Table(name = "catalogo")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "catalogo")
public class Catalogo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "catalogo")
    private Set<CatalogoElemento> catalogoElementos = new HashSet<>();

    @OneToMany(mappedBy = "catalogo")
    private Set<Campo> campos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Catalogo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Catalogo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<CatalogoElemento> getCatalogoElementos() {
        return catalogoElementos;
    }

    public Catalogo catalogoElementos(Set<CatalogoElemento> catalogoElementos) {
        this.catalogoElementos = catalogoElementos;
        return this;
    }

    public Catalogo addCatalogoElemento(CatalogoElemento catalogoElemento) {
        this.catalogoElementos.add(catalogoElemento);
        catalogoElemento.setCatalogo(this);
        return this;
    }

    public Catalogo removeCatalogoElemento(CatalogoElemento catalogoElemento) {
        this.catalogoElementos.remove(catalogoElemento);
        catalogoElemento.setCatalogo(null);
        return this;
    }

    public void setCatalogoElementos(Set<CatalogoElemento> catalogoElementos) {
        this.catalogoElementos = catalogoElementos;
    }

    public Set<Campo> getCampos() {
        return campos;
    }

    public Catalogo campos(Set<Campo> campos) {
        this.campos = campos;
        return this;
    }

    public Catalogo addCampo(Campo campo) {
        this.campos.add(campo);
        campo.setCatalogo(this);
        return this;
    }

    public Catalogo removeCampo(Campo campo) {
        this.campos.remove(campo);
        campo.setCatalogo(null);
        return this;
    }

    public void setCampos(Set<Campo> campos) {
        this.campos = campos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogo)) {
            return false;
        }
        return id != null && id.equals(((Catalogo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Catalogo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
