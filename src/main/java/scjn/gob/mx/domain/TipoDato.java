package scjn.gob.mx.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoDato.
 */
@Entity
@Table(name = "tipo_dato")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tipodato")
public class TipoDato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "tipoDato")
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

    public TipoDato nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Campo> getCampos() {
        return campos;
    }

    public TipoDato campos(Set<Campo> campos) {
        this.campos = campos;
        return this;
    }

    public TipoDato addCampo(Campo campo) {
        this.campos.add(campo);
        campo.setTipoDato(this);
        return this;
    }

    public TipoDato removeCampo(Campo campo) {
        this.campos.remove(campo);
        campo.setTipoDato(null);
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
        if (!(o instanceof TipoDato)) {
            return false;
        }
        return id != null && id.equals(((TipoDato) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TipoDato{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
