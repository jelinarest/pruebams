package scjn.gob.mx.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.Plantilla} entity.
 */
public class PlantillaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @Size(max = 2000)
    private String descripcion;

    private Integer numeroColumnas;

    private Boolean activo;


    private Long tipoPlantillaId;

    private String tipoPlantillaNombre;

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

    public Integer getNumeroColumnas() {
        return numeroColumnas;
    }

    public void setNumeroColumnas(Integer numeroColumnas) {
        this.numeroColumnas = numeroColumnas;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Long getTipoPlantillaId() {
        return tipoPlantillaId;
    }

    public void setTipoPlantillaId(Long tipoPlantillaId) {
        this.tipoPlantillaId = tipoPlantillaId;
    }

    public String getTipoPlantillaNombre() {
        return tipoPlantillaNombre;
    }

    public void setTipoPlantillaNombre(String tipoPlantillaNombre) {
        this.tipoPlantillaNombre = tipoPlantillaNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlantillaDTO plantillaDTO = (PlantillaDTO) o;
        if (plantillaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), plantillaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlantillaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", numeroColumnas=" + getNumeroColumnas() +
            ", activo='" + isActivo() + "'" +
            ", tipoPlantilla=" + getTipoPlantillaId() +
            ", tipoPlantilla='" + getTipoPlantillaNombre() + "'" +
            "}";
    }
}
