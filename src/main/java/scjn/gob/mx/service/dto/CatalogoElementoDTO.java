package scjn.gob.mx.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.CatalogoElemento} entity.
 */
public class CatalogoElementoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private Boolean activo;


    private Long catalogoId;

    private String catalogoNombre;

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

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Long getCatalogoId() {
        return catalogoId;
    }

    public void setCatalogoId(Long catalogoId) {
        this.catalogoId = catalogoId;
    }

    public String getCatalogoNombre() {
        return catalogoNombre;
    }

    public void setCatalogoNombre(String catalogoNombre) {
        this.catalogoNombre = catalogoNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatalogoElementoDTO catalogoElementoDTO = (CatalogoElementoDTO) o;
        if (catalogoElementoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), catalogoElementoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CatalogoElementoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            ", catalogo=" + getCatalogoId() +
            ", catalogo='" + getCatalogoNombre() + "'" +
            "}";
    }
}
