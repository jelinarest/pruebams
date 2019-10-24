package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.CronogramaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cronograma} and its DTO {@link CronogramaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CronogramaMapper extends EntityMapper<CronogramaDTO, Cronograma> {


    @Mapping(target = "cronogramaEtapas", ignore = true)
    @Mapping(target = "removeCronogramaEtapa", ignore = true)
    Cronograma toEntity(CronogramaDTO cronogramaDTO);

    default Cronograma fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cronograma cronograma = new Cronograma();
        cronograma.setId(id);
        return cronograma;
    }
}
