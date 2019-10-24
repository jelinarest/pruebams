package scjn.gob.mx.service;

import scjn.gob.mx.domain.Cronograma;
import scjn.gob.mx.repository.CronogramaRepository;
import scjn.gob.mx.repository.search.CronogramaSearchRepository;
import scjn.gob.mx.service.dto.CronogramaDTO;
import scjn.gob.mx.service.mapper.CronogramaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Cronograma}.
 */
@Service
@Transactional
public class CronogramaService {

    private final Logger log = LoggerFactory.getLogger(CronogramaService.class);

    private final CronogramaRepository cronogramaRepository;

    private final CronogramaMapper cronogramaMapper;

    private final CronogramaSearchRepository cronogramaSearchRepository;

    public CronogramaService(CronogramaRepository cronogramaRepository, CronogramaMapper cronogramaMapper, CronogramaSearchRepository cronogramaSearchRepository) {
        this.cronogramaRepository = cronogramaRepository;
        this.cronogramaMapper = cronogramaMapper;
        this.cronogramaSearchRepository = cronogramaSearchRepository;
    }

    /**
     * Save a cronograma.
     *
     * @param cronogramaDTO the entity to save.
     * @return the persisted entity.
     */
    public CronogramaDTO save(CronogramaDTO cronogramaDTO) {
        log.debug("Request to save Cronograma : {}", cronogramaDTO);
        Cronograma cronograma = cronogramaMapper.toEntity(cronogramaDTO);
        cronograma = cronogramaRepository.save(cronograma);
        CronogramaDTO result = cronogramaMapper.toDto(cronograma);
        cronogramaSearchRepository.save(cronograma);
        return result;
    }

    /**
     * Get all the cronogramas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronogramaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cronogramas");
        return cronogramaRepository.findAll(pageable)
            .map(cronogramaMapper::toDto);
    }


    /**
     * Get one cronograma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CronogramaDTO> findOne(Long id) {
        log.debug("Request to get Cronograma : {}", id);
        return cronogramaRepository.findById(id)
            .map(cronogramaMapper::toDto);
    }

    /**
     * Delete the cronograma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cronograma : {}", id);
        cronogramaRepository.deleteById(id);
        cronogramaSearchRepository.deleteById(id);
    }

    /**
     * Search for the cronograma corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CronogramaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cronogramas for query {}", query);
        return cronogramaSearchRepository.search(queryStringQuery(query), pageable)
            .map(cronogramaMapper::toDto);
    }
}
