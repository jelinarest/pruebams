package scjn.gob.mx.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A CronogramaEtapa.
 */
@Entity
@Table(name = "cronograma_etapa")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cronogramaetapa")
public class CronogramaEtapa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "notas")
    private String notas;

    @Column(name = "fecha_alta")
    private ZonedDateTime fechaAlta;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "orden")
    private Integer orden;

    @OneToMany(mappedBy = "cronogramaEtapa")
    private Set<CronogramaEtapaActividad> cronogramaEtapaActividads = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cronogramaEtapas")
    private Cronograma cronograma;

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

    public CronogramaEtapa nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public CronogramaEtapa notas(String notas) {
        this.notas = notas;
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ZonedDateTime getFechaAlta() {
        return fechaAlta;
    }

    public CronogramaEtapa fechaAlta(ZonedDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
        return this;
    }

    public void setFechaAlta(ZonedDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Boolean isActivo() {
        return activo;
    }

    public CronogramaEtapa activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getOrden() {
        return orden;
    }

    public CronogramaEtapa orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<CronogramaEtapaActividad> getCronogramaEtapaActividads() {
        return cronogramaEtapaActividads;
    }

    public CronogramaEtapa cronogramaEtapaActividads(Set<CronogramaEtapaActividad> cronogramaEtapaActividads) {
        this.cronogramaEtapaActividads = cronogramaEtapaActividads;
        return this;
    }

    public CronogramaEtapa addCronogramaEtapaActividad(CronogramaEtapaActividad cronogramaEtapaActividad) {
        this.cronogramaEtapaActividads.add(cronogramaEtapaActividad);
        cronogramaEtapaActividad.setCronogramaEtapa(this);
        return this;
    }

    public CronogramaEtapa removeCronogramaEtapaActividad(CronogramaEtapaActividad cronogramaEtapaActividad) {
        this.cronogramaEtapaActividads.remove(cronogramaEtapaActividad);
        cronogramaEtapaActividad.setCronogramaEtapa(null);
        return this;
    }

    public void setCronogramaEtapaActividads(Set<CronogramaEtapaActividad> cronogramaEtapaActividads) {
        this.cronogramaEtapaActividads = cronogramaEtapaActividads;
    }

    public Cronograma getCronograma() {
        return cronograma;
    }

    public CronogramaEtapa cronograma(Cronograma cronograma) {
        this.cronograma = cronograma;
        return this;
    }

    public void setCronograma(Cronograma cronograma) {
        this.cronograma = cronograma;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronogramaEtapa)) {
            return false;
        }
        return id != null && id.equals(((CronogramaEtapa) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CronogramaEtapa{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", notas='" + getNotas() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", activo='" + isActivo() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}
