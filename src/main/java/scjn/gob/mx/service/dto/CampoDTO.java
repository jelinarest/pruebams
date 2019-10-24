package scjn.gob.mx.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link scjn.gob.mx.domain.Campo} entity.
 */
public class CampoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private Boolean multiSeleccion;

    private Boolean requerido;

    private Integer longitud;

    private Boolean dependienteVisibilidad;


    private Long datoId;

    private String datoNombre;

    private Long tipoDatoId;

    private String tipoDatoNombre;

    private Long tipoValidacionId;

    private String tipoValidacionNombre;

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

    public Boolean isMultiSeleccion() {
        return multiSeleccion;
    }

    public void setMultiSeleccion(Boolean multiSeleccion) {
        this.multiSeleccion = multiSeleccion;
    }

    public Boolean isRequerido() {
        return requerido;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Integer getLongitud() {
        return longitud;
    }

    public void setLongitud(Integer longitud) {
        this.longitud = longitud;
    }

    public Boolean isDependienteVisibilidad() {
        return dependienteVisibilidad;
    }

    public void setDependienteVisibilidad(Boolean dependienteVisibilidad) {
        this.dependienteVisibilidad = dependienteVisibilidad;
    }

    public Long getDatoId() {
        return datoId;
    }

    public void setDatoId(Long datoId) {
        this.datoId = datoId;
    }

    public String getDatoNombre() {
        return datoNombre;
    }

    public void setDatoNombre(String datoNombre) {
        this.datoNombre = datoNombre;
    }

    public Long getTipoDatoId() {
        return tipoDatoId;
    }

    public void setTipoDatoId(Long tipoDatoId) {
        this.tipoDatoId = tipoDatoId;
    }

    public String getTipoDatoNombre() {
        return tipoDatoNombre;
    }

    public void setTipoDatoNombre(String tipoDatoNombre) {
        this.tipoDatoNombre = tipoDatoNombre;
    }

    public Long getTipoValidacionId() {
        return tipoValidacionId;
    }

    public void setTipoValidacionId(Long tipoValidacionId) {
        this.tipoValidacionId = tipoValidacionId;
    }

    public String getTipoValidacionNombre() {
        return tipoValidacionNombre;
    }

    public void setTipoValidacionNombre(String tipoValidacionNombre) {
        this.tipoValidacionNombre = tipoValidacionNombre;
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

        CampoDTO campoDTO = (CampoDTO) o;
        if (campoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CampoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", multiSeleccion='" + isMultiSeleccion() + "'" +
            ", requerido='" + isRequerido() + "'" +
            ", longitud=" + getLongitud() +
            ", dependienteVisibilidad='" + isDependienteVisibilidad() + "'" +
            ", dato=" + getDatoId() +
            ", dato='" + getDatoNombre() + "'" +
            ", tipoDato=" + getTipoDatoId() +
            ", tipoDato='" + getTipoDatoNombre() + "'" +
            ", tipoValidacion=" + getTipoValidacionId() +
            ", tipoValidacion='" + getTipoValidacionNombre() + "'" +
            ", catalogo=" + getCatalogoId() +
            ", catalogo='" + getCatalogoNombre() + "'" +
            "}";
    }
}
