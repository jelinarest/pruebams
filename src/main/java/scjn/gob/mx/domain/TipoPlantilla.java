package scjn.gob.mx.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TipoPlantilla.
 */
@Entity
@Table(name = "tipo_plantilla")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tipoplantilla")
public class TipoPlantilla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "tipoPlantilla")
    private Set<Plantilla> plantillas = new HashSet<>();

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

    public TipoPlantilla nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Plantilla> getPlantillas() {
        return plantillas;
    }

    public TipoPlantilla plantillas(Set<Plantilla> plantillas) {
        this.plantillas = plantillas;
        return this;
    }

    public TipoPlantilla addPlantilla(Plantilla plantilla) {
        this.plantillas.add(plantilla);
        plantilla.setTipoPlantilla(this);
        return this;
    }

    public TipoPlantilla removePlantilla(Plantilla plantilla) {
        this.plantillas.remove(plantilla);
        plantilla.setTipoPlantilla(null);
        return this;
    }

    public void setPlantillas(Set<Plantilla> plantillas) {
        this.plantillas = plantillas;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoPlantilla)) {
            return false;
        }
        return id != null && id.equals(((TipoPlantilla) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TipoPlantilla{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
