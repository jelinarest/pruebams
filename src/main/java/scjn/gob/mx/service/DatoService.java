package scjn.gob.mx.service;

import scjn.gob.mx.domain.Dato;
import scjn.gob.mx.repository.DatoRepository;
import scjn.gob.mx.repository.search.DatoSearchRepository;
import scjn.gob.mx.service.dto.DatoDTO;
import scjn.gob.mx.service.mapper.DatoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Dato}.
 */
@Service
@Transactional
public class DatoService {

    private final Logger log = LoggerFactory.getLogger(DatoService.class);

    private final DatoRepository datoRepository;

    private final DatoMapper datoMapper;

    private final DatoSearchRepository datoSearchRepository;

    public DatoService(DatoRepository datoRepository, DatoMapper datoMapper, DatoSearchRepository datoSearchRepository) {
        this.datoRepository = datoRepository;
        this.datoMapper = datoMapper;
        this.datoSearchRepository = datoSearchRepository;
    }

    /**
     * Save a dato.
     *
     * @param datoDTO the entity to save.
     * @return the persisted entity.
     */
    public DatoDTO save(DatoDTO datoDTO) {
        log.debug("Request to save Dato : {}", datoDTO);
        Dato dato = datoMapper.toEntity(datoDTO);
        dato = datoRepository.save(dato);
        DatoDTO result = datoMapper.toDto(dato);
        datoSearchRepository.save(dato);
        return result;
    }

    /**
     * Get all the datoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DatoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Datoes");
        log.debug("Test");
        return datoRepository.findAll(pageable)
            .map(datoMapper::toDto);
    }


    /**
     * Get one dato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DatoDTO> findOne(Long id) {
        log.debug("Request to get Dato : {}", id);
        return datoRepository.findById(id)
            .map(datoMapper::toDto);
    }

    /**
     * Delete the dato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dato : {}", id);
        datoRepository.deleteById(id);
        datoSearchRepository.deleteById(id);
    }

    /**
     * Search for the dato corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DatoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Datoes for query {}", query);
        return datoSearchRepository.search(queryStringQuery(query), pageable)
            .map(datoMapper::toDto);
    }
}
