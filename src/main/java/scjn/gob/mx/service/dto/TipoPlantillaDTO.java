package scjn.gob.mx.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.TipoPlantilla} entity.
 */
public class TipoPlantillaDTO implements Serializable {

    private Long id;

    private String nombre;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TipoPlantillaDTO tipoPlantillaDTO = (TipoPlantillaDTO) o;
        if (tipoPlantillaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoPlantillaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoPlantillaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
