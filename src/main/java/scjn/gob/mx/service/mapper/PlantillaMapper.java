package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.PlantillaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plantilla} and its DTO {@link PlantillaDTO}.
 */
@Mapper(componentModel = "spring", uses = {TipoPlantillaMapper.class})
public interface PlantillaMapper extends EntityMapper<PlantillaDTO, Plantilla> {

    @Mapping(source = "tipoPlantilla.id", target = "tipoPlantillaId")
    @Mapping(source = "tipoPlantilla.nombre", target = "tipoPlantillaNombre")
    PlantillaDTO toDto(Plantilla plantilla);

    @Mapping(source = "tipoPlantillaId", target = "tipoPlantilla")
    Plantilla toEntity(PlantillaDTO plantillaDTO);

    default Plantilla fromId(Long id) {
        if (id == null) {
            return null;
        }
        Plantilla plantilla = new Plantilla();
        plantilla.setId(id);
        return plantilla;
    }
}
