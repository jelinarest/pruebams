package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.CampoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Campo} and its DTO {@link CampoDTO}.
 */
@Mapper(componentModel = "spring", uses = {DatoMapper.class, TipoDatoMapper.class, TipoValidacionMapper.class, CatalogoMapper.class})
public interface CampoMapper extends EntityMapper<CampoDTO, Campo> {

    @Mapping(source = "dato.id", target = "datoId")
    @Mapping(source = "dato.nombre", target = "datoNombre")
    @Mapping(source = "tipoDato.id", target = "tipoDatoId")
    @Mapping(source = "tipoDato.nombre", target = "tipoDatoNombre")
    @Mapping(source = "tipoValidacion.id", target = "tipoValidacionId")
    @Mapping(source = "tipoValidacion.nombre", target = "tipoValidacionNombre")
    @Mapping(source = "catalogo.id", target = "catalogoId")
    @Mapping(source = "catalogo.nombre", target = "catalogoNombre")
    CampoDTO toDto(Campo campo);

    @Mapping(source = "datoId", target = "dato")
    @Mapping(source = "tipoDatoId", target = "tipoDato")
    @Mapping(source = "tipoValidacionId", target = "tipoValidacion")
    @Mapping(source = "catalogoId", target = "catalogo")
    Campo toEntity(CampoDTO campoDTO);

    default Campo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Campo campo = new Campo();
        campo.setId(id);
        return campo;
    }
}
