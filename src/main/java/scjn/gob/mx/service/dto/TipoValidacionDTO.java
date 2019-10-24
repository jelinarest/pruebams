package scjn.gob.mx.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.TipoValidacion} entity.
 */
public class TipoValidacionDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    @Size(max = 2000)
    private String expresionRegular;

    private String textoAyuda;


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

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public void setExpresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
    }

    public String getTextoAyuda() {
        return textoAyuda;
    }

    public void setTextoAyuda(String textoAyuda) {
        this.textoAyuda = textoAyuda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TipoValidacionDTO tipoValidacionDTO = (TipoValidacionDTO) o;
        if (tipoValidacionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoValidacionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoValidacionDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", expresionRegular='" + getExpresionRegular() + "'" +
            ", textoAyuda='" + getTextoAyuda() + "'" +
            "}";
    }
}
