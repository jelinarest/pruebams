package scjn.gob.mx.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Campo.
 */
@Entity
@Table(name = "campo")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "campo")
public class Campo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "multi_seleccion")
    private Boolean multiSeleccion;

    @Column(name = "requerido")
    private Boolean requerido;

    @Column(name = "longitud")
    private Integer longitud;

    @Column(name = "dependiente_visibilidad")
    private Boolean dependienteVisibilidad;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("campos")
    private Dato dato;

    @ManyToOne
    @JsonIgnoreProperties("campos")
    private TipoDato tipoDato;

    @ManyToOne
    @JsonIgnoreProperties("campos")
    private TipoValidacion tipoValidacion;

    @ManyToOne
    @JsonIgnoreProperties("campos")
    private Catalogo catalogo;

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

    public Campo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isMultiSeleccion() {
        return multiSeleccion;
    }

    public Campo multiSeleccion(Boolean multiSeleccion) {
        this.multiSeleccion = multiSeleccion;
        return this;
    }

    public void setMultiSeleccion(Boolean multiSeleccion) {
        this.multiSeleccion = multiSeleccion;
    }

    public Boolean isRequerido() {
        return requerido;
    }

    public Campo requerido(Boolean requerido) {
        this.requerido = requerido;
        return this;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Integer getLongitud() {
        return longitud;
    }

    public Campo longitud(Integer longitud) {
        this.longitud = longitud;
        return this;
    }

    public void setLongitud(Integer longitud) {
        this.longitud = longitud;
    }

    public Boolean isDependienteVisibilidad() {
        return dependienteVisibilidad;
    }

    public Campo dependienteVisibilidad(Boolean dependienteVisibilidad) {
        this.dependienteVisibilidad = dependienteVisibilidad;
        return this;
    }

    public void setDependienteVisibilidad(Boolean dependienteVisibilidad) {
        this.dependienteVisibilidad = dependienteVisibilidad;
    }

    public Dato getDato() {
        return dato;
    }

    public Campo dato(Dato dato) {
        this.dato = dato;
        return this;
    }

    public void setDato(Dato dato) {
        this.dato = dato;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public Campo tipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
        return this;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public TipoValidacion getTipoValidacion() {
        return tipoValidacion;
    }

    public Campo tipoValidacion(TipoValidacion tipoValidacion) {
        this.tipoValidacion = tipoValidacion;
        return this;
    }

    public void setTipoValidacion(TipoValidacion tipoValidacion) {
        this.tipoValidacion = tipoValidacion;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public Campo catalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
        return this;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campo)) {
            return false;
        }
        return id != null && id.equals(((Campo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Campo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", multiSeleccion='" + isMultiSeleccion() + "'" +
            ", requerido='" + isRequerido() + "'" +
            ", longitud=" + getLongitud() +
            ", dependienteVisibilidad='" + isDependienteVisibilidad() + "'" +
            "}";
    }
}
