package scjn.gob.mx.service;

import scjn.gob.mx.domain.CronogramaEtapa;
import scjn.gob.mx.repository.CronogramaEtapaRepository;
import scjn.gob.mx.repository.search.CronogramaEtapaSearchRepository;
import scjn.gob.mx.service.dto.CronogramaEtapaDTO;
import scjn.gob.mx.service.mapper.CronogramaEtapaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CronogramaEtapa}.
 */
@Service
@Transactional
public class CronogramaEtapaService {

    private final Logger log = LoggerFactory.getLogger(CronogramaEtapaService.class);

    private final CronogramaEtapaRepository cronogramaEtapaRepository;

    private final CronogramaEtapaMapper cronogramaEtapaMapper;

    private final CronogramaEtapaSearchRepository cronogramaEtapaSearchRepository;

    public CronogramaEtapaService(CronogramaEtapaRepository cronogramaEtapaRepository, CronogramaEtapaMapper cronogramaEtapaMapper, CronogramaEtapaSearchRepository cronogramaEtapaSearchRepository) {
        this.cronogramaEtapaRepository = cronogramaEtapaRepository;
        this.cronogramaEtapaMapper = cronogramaEtapaMapper;
        this.cronogramaEtapaSearchRepository = cronogramaEtapaSearchRepository;
    }

    /**
     * Save a cronogramaEtapa.
     *
     * @param cronogramaEtapaDTO the entity to save.
     * @return the persisted entity.
     */
    public CronogramaEtapaDTO save(CronogramaEtapaDTO cronogramaEtapaDTO) {
        log.debug("Request to save CronogramaEtapa : {}", cronogramaEtapaDTO);
        CronogramaEtapa cronogramaEtapa = cronogramaEtapaMapper.toEntity(cronogramaEtapaDTO);
        cronogramaEtapa = cronogramaEtapaRepository.save(cronogramaEtapa);
        CronogramaEtapaDTO result = cronogramaEtapaMapper.toDto(cronogramaEtapa);
        cronogramaEtapaSearchRepository.save(cronogramaEtapa);
        return result;
    }

    /**
     * Get all the cronogramaEtapas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronogramaEtapaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CronogramaEtapas");
        return cronogramaEtapaRepository.findAll(pageable)
            .map(cronogramaEtapaMapper::toDto);
    }


    /**
     * Get one cronogramaEtapa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CronogramaEtapaDTO> findOne(Long id) {
        log.debug("Request to get CronogramaEtapa : {}", id);
        return cronogramaEtapaRepository.findById(id)
            .map(cronogramaEtapaMapper::toDto);
    }

    /**
     * Delete the cronogramaEtapa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CronogramaEtapa : {}", id);
        cronogramaEtapaRepository.deleteById(id);
        cronogramaEtapaSearchRepository.deleteById(id);
    }

    /**
     * Search for the cronogramaEtapa corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronogramaEtapaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CronogramaEtapas for query {}", query);
        return cronogramaEtapaSearchRepository.search(queryStringQuery(query), pageable)
            .map(cronogramaEtapaMapper::toDto);
    }
}
