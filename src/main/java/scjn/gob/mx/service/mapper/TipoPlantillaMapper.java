package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.TipoPlantillaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoPlantilla} and its DTO {@link TipoPlantillaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoPlantillaMapper extends EntityMapper<TipoPlantillaDTO, TipoPlantilla> {


    @Mapping(target = "plantillas", ignore = true)
    @Mapping(target = "removePlantilla", ignore = true)
    TipoPlantilla toEntity(TipoPlantillaDTO tipoPlantillaDTO);

    default TipoPlantilla fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoPlantilla tipoPlantilla = new TipoPlantilla();
        tipoPlantilla.setId(id);
        return tipoPlantilla;
    }
}
