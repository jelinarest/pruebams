package scjn.gob.mx.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Plantilla.
 */
@Entity
@Table(name = "plantilla")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plantilla")
public class Plantilla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 2000)
    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @Column(name = "numero_columnas")
    private Integer numeroColumnas;

    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("plantillas")
    private TipoPlantilla tipoPlantilla;

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

    public Plantilla nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Plantilla descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getNumeroColumnas() {
        return numeroColumnas;
    }

    public Plantilla numeroColumnas(Integer numeroColumnas) {
        this.numeroColumnas = numeroColumnas;
        return this;
    }

    public void setNumeroColumnas(Integer numeroColumnas) {
        this.numeroColumnas = numeroColumnas;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Plantilla activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public TipoPlantilla getTipoPlantilla() {
        return tipoPlantilla;
    }

    public Plantilla tipoPlantilla(TipoPlantilla tipoPlantilla) {
        this.tipoPlantilla = tipoPlantilla;
        return this;
    }

    public void setTipoPlantilla(TipoPlantilla tipoPlantilla) {
        this.tipoPlantilla = tipoPlantilla;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plantilla)) {
            return false;
        }
        return id != null && id.equals(((Plantilla) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plantilla{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", numeroColumnas=" + getNumeroColumnas() +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
