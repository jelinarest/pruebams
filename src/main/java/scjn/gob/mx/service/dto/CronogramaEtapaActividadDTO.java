package scjn.gob.mx.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.CronogramaEtapaActividad} entity.
 */
public class CronogramaEtapaActividadDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer anio;

    @NotNull
    private Integer mes;

    @NotNull
    private Integer semana;

    private Boolean realizado;


    private Long cronogramaEtapaId;

    private String cronogramaEtapaNombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getSemana() {
        return semana;
    }

    public void setSemana(Integer semana) {
        this.semana = semana;
    }

    public Boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public Long getCronogramaEtapaId() {
        return cronogramaEtapaId;
    }

    public void setCronogramaEtapaId(Long cronogramaEtapaId) {
        this.cronogramaEtapaId = cronogramaEtapaId;
    }

    public String getCronogramaEtapaNombre() {
        return cronogramaEtapaNombre;
    }

    public void setCronogramaEtapaNombre(String cronogramaEtapaNombre) {
        this.cronogramaEtapaNombre = cronogramaEtapaNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = (CronogramaEtapaActividadDTO) o;
        if (cronogramaEtapaActividadDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cronogramaEtapaActividadDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CronogramaEtapaActividadDTO{" +
            "id=" + getId() +
            ", anio=" + getAnio() +
            ", mes=" + getMes() +
            ", semana=" + getSemana() +
            ", realizado='" + isRealizado() + "'" +
            ", cronogramaEtapa=" + getCronogramaEtapaId() +
            ", cronogramaEtapa='" + getCronogramaEtapaNombre() + "'" +
            "}";
    }
}
