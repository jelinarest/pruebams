package scjn.gob.mx.service;

import scjn.gob.mx.domain.TipoValidacion;
import scjn.gob.mx.repository.TipoValidacionRepository;
import scjn.gob.mx.repository.search.TipoValidacionSearchRepository;
import scjn.gob.mx.service.dto.TipoValidacionDTO;
import scjn.gob.mx.service.mapper.TipoValidacionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TipoValidacion}.
 */
@Service
@Transactional
public class TipoValidacionService {

    private final Logger log = LoggerFactory.getLogger(TipoValidacionService.class);

    private final TipoValidacionRepository tipoValidacionRepository;

    private final TipoValidacionMapper tipoValidacionMapper;

    private final TipoValidacionSearchRepository tipoValidacionSearchRepository;

    public TipoValidacionService(TipoValidacionRepository tipoValidacionRepository, TipoValidacionMapper tipoValidacionMapper, TipoValidacionSearchRepository tipoValidacionSearchRepository) {
        this.tipoValidacionRepository = tipoValidacionRepository;
        this.tipoValidacionMapper = tipoValidacionMapper;
        this.tipoValidacionSearchRepository = tipoValidacionSearchRepository;
    }

    /**
     * Save a tipoValidacion.
     *
     * @param tipoValidacionDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoValidacionDTO save(TipoValidacionDTO tipoValidacionDTO) {
        log.debug("Request to save TipoValidacion : {}", tipoValidacionDTO);
        TipoValidacion tipoValidacion = tipoValidacionMapper.toEntity(tipoValidacionDTO);
        tipoValidacion = tipoValidacionRepository.save(tipoValidacion);
        TipoValidacionDTO result = tipoValidacionMapper.toDto(tipoValidacion);
        tipoValidacionSearchRepository.save(tipoValidacion);
        return result;
    }

    /**
     * Get all the tipoValidacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoValidacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoValidacions");
        return tipoValidacionRepository.findAll(pageable)
            .map(tipoValidacionMapper::toDto);
    }


    /**
     * Get one tipoValidacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoValidacionDTO> findOne(Long id) {
        log.debug("Request to get TipoValidacion : {}", id);
        return tipoValidacionRepository.findById(id)
            .map(tipoValidacionMapper::toDto);
    }

    /**
     * Delete the tipoValidacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TipoValidacion : {}", id);
        tipoValidacionRepository.deleteById(id);
        tipoValidacionSearchRepository.deleteById(id);
    }

    /**
     * Search for the tipoValidacion corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoValidacionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoValidacions for query {}", query);
        return tipoValidacionSearchRepository.search(queryStringQuery(query), pageable)
            .map(tipoValidacionMapper::toDto);
    }
}
