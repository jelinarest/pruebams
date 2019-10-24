package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.DatoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dato} and its DTO {@link DatoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DatoMapper extends EntityMapper<DatoDTO, Dato> {


    @Mapping(target = "campos", ignore = true)
    @Mapping(target = "removeCampo", ignore = true)
    Dato toEntity(DatoDTO datoDTO);

    default Dato fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dato dato = new Dato();
        dato.setId(id);
        return dato;
    }
}
