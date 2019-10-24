package scjn.gob.mx.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.Cronograma} entity.
 */
public class CronogramaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nombre;

    private String descripcion;

    private String motivoAjuste;

    private Boolean activo;

    private ZonedDateTime fechaAlta;


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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMotivoAjuste() {
        return motivoAjuste;
    }

    public void setMotivoAjuste(String motivoAjuste) {
        this.motivoAjuste = motivoAjuste;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ZonedDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(ZonedDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CronogramaDTO cronogramaDTO = (CronogramaDTO) o;
        if (cronogramaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cronogramaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CronogramaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", motivoAjuste='" + getMotivoAjuste() + "'" +
            ", activo='" + isActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            "}";
    }
}
