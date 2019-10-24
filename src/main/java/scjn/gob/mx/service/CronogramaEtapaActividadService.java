package scjn.gob.mx.service;

import scjn.gob.mx.domain.CronogramaEtapaActividad;
import scjn.gob.mx.repository.CronogramaEtapaActividadRepository;
import scjn.gob.mx.repository.search.CronogramaEtapaActividadSearchRepository;
import scjn.gob.mx.service.dto.CronogramaEtapaActividadDTO;
import scjn.gob.mx.service.mapper.CronogramaEtapaActividadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CronogramaEtapaActividad}.
 */
@Service
@Transactional
public class CronogramaEtapaActividadService {

    private final Logger log = LoggerFactory.getLogger(CronogramaEtapaActividadService.class);

    private final CronogramaEtapaActividadRepository cronogramaEtapaActividadRepository;

    private final CronogramaEtapaActividadMapper cronogramaEtapaActividadMapper;

    private final CronogramaEtapaActividadSearchRepository cronogramaEtapaActividadSearchRepository;

    public CronogramaEtapaActividadService(CronogramaEtapaActividadRepository cronogramaEtapaActividadRepository, CronogramaEtapaActividadMapper cronogramaEtapaActividadMapper, CronogramaEtapaActividadSearchRepository cronogramaEtapaActividadSearchRepository) {
        this.cronogramaEtapaActividadRepository = cronogramaEtapaActividadRepository;
        this.cronogramaEtapaActividadMapper = cronogramaEtapaActividadMapper;
        this.cronogramaEtapaActividadSearchRepository = cronogramaEtapaActividadSearchRepository;
    }

    /**
     * Save a cronogramaEtapaActividad.
     *
     * @param cronogramaEtapaActividadDTO the entity to save.
     * @return the persisted entity.
     */
    public CronogramaEtapaActividadDTO save(CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO) {
        log.debug("Request to save CronogramaEtapaActividad : {}", cronogramaEtapaActividadDTO);
        CronogramaEtapaActividad cronogramaEtapaActividad = cronogramaEtapaActividadMapper.toEntity(cronogramaEtapaActividadDTO);
        cronogramaEtapaActividad = cronogramaEtapaActividadRepository.save(cronogramaEtapaActividad);
        CronogramaEtapaActividadDTO result = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);
        cronogramaEtapaActividadSearchRepository.save(cronogramaEtapaActividad);
        return result;
    }

    /**
     * Get all the cronogramaEtapaActividads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronogramaEtapaActividadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CronogramaEtapaActividads");
        return cronogramaEtapaActividadRepository.findAll(pageable)
            .map(cronogramaEtapaActividadMapper::toDto);
    }


    /**
     * Get one cronogramaEtapaActividad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CronogramaEtapaActividadDTO> findOne(Long id) {
        log.debug("Request to get CronogramaEtapaActividad : {}", id);
        return cronogramaEtapaActividadRepository.findById(id)
            .map(cronogramaEtapaActividadMapper::toDto);
    }

    /**
     * Delete the cronogramaEtapaActividad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CronogramaEtapaActividad : {}", id);
        cronogramaEtapaActividadRepository.deleteById(id);
        cronogramaEtapaActividadSearchRepository.deleteById(id);
    }

    /**
     * Search for the cronogramaEtapaActividad corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronogramaEtapaActividadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CronogramaEtapaActividads for query {}", query);
        return cronogramaEtapaActividadSearchRepository.search(queryStringQuery(query), pageable)
            .map(cronogramaEtapaActividadMapper::toDto);
    }
}
