package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.CronogramaEtapaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CronogramaEtapa} and its DTO {@link CronogramaEtapaDTO}.
 */
@Mapper(componentModel = "spring", uses = {CronogramaMapper.class})
public interface CronogramaEtapaMapper extends EntityMapper<CronogramaEtapaDTO, CronogramaEtapa> {

    @Mapping(source = "cronograma.id", target = "cronogramaId")
    @Mapping(source = "cronograma.nombre", target = "cronogramaNombre")
    CronogramaEtapaDTO toDto(CronogramaEtapa cronogramaEtapa);

    @Mapping(target = "cronogramaEtapaActividads", ignore = true)
    @Mapping(target = "removeCronogramaEtapaActividad", ignore = true)
    @Mapping(source = "cronogramaId", target = "cronograma")
    CronogramaEtapa toEntity(CronogramaEtapaDTO cronogramaEtapaDTO);

    default CronogramaEtapa fromId(Long id) {
        if (id == null) {
            return null;
        }
        CronogramaEtapa cronogramaEtapa = new CronogramaEtapa();
        cronogramaEtapa.setId(id);
        return cronogramaEtapa;
    }
}
