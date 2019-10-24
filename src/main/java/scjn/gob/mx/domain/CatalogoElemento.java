package scjn.gob.mx.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A CatalogoElemento.
 */
@Entity
@Table(name = "catalogo_elemento")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "catalogoelemento")
public class CatalogoElemento implements Serializable {

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("catalogoElementos")
    private Catalogo catalogo;

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

    public CatalogoElemento nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public CatalogoElemento activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public CatalogoElemento catalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
        return this;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogoElemento)) {
            return false;
        }
        return id != null && id.equals(((CatalogoElemento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CatalogoElemento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
