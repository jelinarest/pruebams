package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.TipoValidacionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoValidacion} and its DTO {@link TipoValidacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoValidacionMapper extends EntityMapper<TipoValidacionDTO, TipoValidacion> {


    @Mapping(target = "campos", ignore = true)
    @Mapping(target = "removeCampo", ignore = true)
    TipoValidacion toEntity(TipoValidacionDTO tipoValidacionDTO);

    default TipoValidacion fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoValidacion tipoValidacion = new TipoValidacion();
        tipoValidacion.setId(id);
        return tipoValidacion;
    }
}
