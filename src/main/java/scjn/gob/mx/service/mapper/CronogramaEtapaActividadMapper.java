package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.CronogramaEtapaActividadDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CronogramaEtapaActividad} and its DTO {@link CronogramaEtapaActividadDTO}.
 */
@Mapper(componentModel = "spring", uses = {CronogramaEtapaMapper.class})
public interface CronogramaEtapaActividadMapper extends EntityMapper<CronogramaEtapaActividadDTO, CronogramaEtapaActividad> {

    @Mapping(source = "cronogramaEtapa.id", target = "cronogramaEtapaId")
    @Mapping(source = "cronogramaEtapa.nombre", target = "cronogramaEtapaNombre")
    CronogramaEtapaActividadDTO toDto(CronogramaEtapaActividad cronogramaEtapaActividad);

    @Mapping(source = "cronogramaEtapaId", target = "cronogramaEtapa")
    CronogramaEtapaActividad toEntity(CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO);

    default CronogramaEtapaActividad fromId(Long id) {
        if (id == null) {
            return null;
        }
        CronogramaEtapaActividad cronogramaEtapaActividad = new CronogramaEtapaActividad();
        cronogramaEtapaActividad.setId(id);
        return cronogramaEtapaActividad;
    }
}
