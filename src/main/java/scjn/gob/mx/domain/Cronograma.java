package scjn.gob.mx.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cronograma.
 */
@Entity
@Table(name = "cronograma")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cronograma")
public class Cronograma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "motivo_ajuste")
    private String motivoAjuste;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_alta")
    private ZonedDateTime fechaAlta;

    @OneToMany(mappedBy = "cronograma")
    private Set<CronogramaEtapa> cronogramaEtapas = new HashSet<>();

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

    public Cronograma nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Cronograma descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMotivoAjuste() {
        return motivoAjuste;
    }

    public Cronograma motivoAjuste(String motivoAjuste) {
        this.motivoAjuste = motivoAjuste;
        return this;
    }

    public void setMotivoAjuste(String motivoAjuste) {
        this.motivoAjuste = motivoAjuste;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Cronograma activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ZonedDateTime getFechaAlta() {
        return fechaAlta;
    }

    public Cronograma fechaAlta(ZonedDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
        return this;
    }

    public void setFechaAlta(ZonedDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<CronogramaEtapa> getCronogramaEtapas() {
        return cronogramaEtapas;
    }

    public Cronograma cronogramaEtapas(Set<CronogramaEtapa> cronogramaEtapas) {
        this.cronogramaEtapas = cronogramaEtapas;
        return this;
    }

    public Cronograma addCronogramaEtapa(CronogramaEtapa cronogramaEtapa) {
        this.cronogramaEtapas.add(cronogramaEtapa);
        cronogramaEtapa.setCronograma(this);
        return this;
    }

    public Cronograma removeCronogramaEtapa(CronogramaEtapa cronogramaEtapa) {
        this.cronogramaEtapas.remove(cronogramaEtapa);
        cronogramaEtapa.setCronograma(null);
        return this;
    }

    public void setCronogramaEtapas(Set<CronogramaEtapa> cronogramaEtapas) {
        this.cronogramaEtapas = cronogramaEtapas;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cronograma)) {
            return false;
        }
        return id != null && id.equals(((Cronograma) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cronograma{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", motivoAjuste='" + getMotivoAjuste() + "'" +
            ", activo='" + isActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            "}";
    }
}
