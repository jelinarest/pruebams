package scjn.gob.mx.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.CronogramaEtapa} entity.
 */
public class CronogramaEtapaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    private String notas;

    private ZonedDateTime fechaAlta;

    private Boolean activo;

    private Integer orden;


    private Long cronogramaId;

    private String cronogramaNombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ZonedDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(ZonedDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getCronogramaId() {
        return cronogramaId;
    }

    public void setCronogramaId(Long cronogramaId) {
        this.cronogramaId = cronogramaId;
    }

    public String getCronogramaNombre() {
        return cronogramaNombre;
    }

    public void setCronogramaNombre(String cronogramaNombre) {
        this.cronogramaNombre = cronogramaNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CronogramaEtapaDTO cronogramaEtapaDTO = (CronogramaEtapaDTO) o;
        if (cronogramaEtapaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cronogramaEtapaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CronogramaEtapaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", notas='" + getNotas() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", activo='" + isActivo() + "'" +
            ", orden=" + getOrden() +
            ", cronograma=" + getCronogramaId() +
            ", cronograma='" + getCronogramaNombre() + "'" +
            "}";
    }
}
