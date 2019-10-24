package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.TipoDatoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoDato} and its DTO {@link TipoDatoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoDatoMapper extends EntityMapper<TipoDatoDTO, TipoDato> {


    @Mapping(target = "campos", ignore = true)
    @Mapping(target = "removeCampo", ignore = true)
    TipoDato toEntity(TipoDatoDTO tipoDatoDTO);

    default TipoDato fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoDato tipoDato = new TipoDato();
        tipoDato.setId(id);
        return tipoDato;
    }
}
