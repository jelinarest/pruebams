package scjn.gob.mx.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoValidacion.
 */
@Entity
@Table(name = "tipo_validacion")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tipovalidacion")
public class TipoValidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 2000)
    @Column(name = "expresion_regular", length = 2000, nullable = false)
    private String expresionRegular;

    @Column(name = "texto_ayuda")
    private String textoAyuda;

    @OneToMany(mappedBy = "tipoValidacion")
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

    public TipoValidacion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public TipoValidacion expresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
        return this;
    }

    public void setExpresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
    }

    public String getTextoAyuda() {
        return textoAyuda;
    }

    public TipoValidacion textoAyuda(String textoAyuda) {
        this.textoAyuda = textoAyuda;
        return this;
    }

    public void setTextoAyuda(String textoAyuda) {
        this.textoAyuda = textoAyuda;
    }

    public Set<Campo> getCampos() {
        return campos;
    }

    public TipoValidacion campos(Set<Campo> campos) {
        this.campos = campos;
        return this;
    }

    public TipoValidacion addCampo(Campo campo) {
        this.campos.add(campo);
        campo.setTipoValidacion(this);
        return this;
    }

    public TipoValidacion removeCampo(Campo campo) {
        this.campos.remove(campo);
        campo.setTipoValidacion(null);
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
        if (!(o instanceof TipoValidacion)) {
            return false;
        }
        return id != null && id.equals(((TipoValidacion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TipoValidacion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", expresionRegular='" + getExpresionRegular() + "'" +
            ", textoAyuda='" + getTextoAyuda() + "'" +
            "}";
    }
}
