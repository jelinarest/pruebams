package scjn.gob.mx.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A CronogramaEtapaActividad.
 */
@Entity
@Table(name = "cronograma_etapa_actividad")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cronogramaetapaactividad")
public class CronogramaEtapaActividad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "anio", nullable = false)
    private Integer anio;

    @NotNull
    @Column(name = "mes", nullable = false)
    private Integer mes;

    @NotNull
    @Column(name = "semana", nullable = false)
    private Integer semana;

    @Column(name = "realizado")
    private Boolean realizado;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cronogramaEtapaActividads")
    private CronogramaEtapa cronogramaEtapa;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public CronogramaEtapaActividad anio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public CronogramaEtapaActividad mes(Integer mes) {
        this.mes = mes;
        return this;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getSemana() {
        return semana;
    }

    public CronogramaEtapaActividad semana(Integer semana) {
        this.semana = semana;
        return this;
    }

    public void setSemana(Integer semana) {
        this.semana = semana;
    }

    public Boolean isRealizado() {
        return realizado;
    }

    public CronogramaEtapaActividad realizado(Boolean realizado) {
        this.realizado = realizado;
        return this;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public CronogramaEtapa getCronogramaEtapa() {
        return cronogramaEtapa;
    }

    public CronogramaEtapaActividad cronogramaEtapa(CronogramaEtapa cronogramaEtapa) {
        this.cronogramaEtapa = cronogramaEtapa;
        return this;
    }

    public void setCronogramaEtapa(CronogramaEtapa cronogramaEtapa) {
        this.cronogramaEtapa = cronogramaEtapa;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CronogramaEtapaActividad)) {
            return false;
        }
        return id != null && id.equals(((CronogramaEtapaActividad) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CronogramaEtapaActividad{" +
            "id=" + getId() +
            ", anio=" + getAnio() +
            ", mes=" + getMes() +
            ", semana=" + getSemana() +
            ", realizado='" + isRealizado() + "'" +
            "}";
    }
}
