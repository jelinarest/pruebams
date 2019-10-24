package scjn.gob.mx.service;

import scjn.gob.mx.domain.TipoDato;
import scjn.gob.mx.repository.TipoDatoRepository;
import scjn.gob.mx.repository.search.TipoDatoSearchRepository;
import scjn.gob.mx.service.dto.TipoDatoDTO;
import scjn.gob.mx.service.mapper.TipoDatoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TipoDato}.
 */
@Service
@Transactional
public class TipoDatoService {

    private final Logger log = LoggerFactory.getLogger(TipoDatoService.class);

    private final TipoDatoRepository tipoDatoRepository;

    private final TipoDatoMapper tipoDatoMapper;

    private final TipoDatoSearchRepository tipoDatoSearchRepository;

    public TipoDatoService(TipoDatoRepository tipoDatoRepository, TipoDatoMapper tipoDatoMapper, TipoDatoSearchRepository tipoDatoSearchRepository) {
        this.tipoDatoRepository = tipoDatoRepository;
        this.tipoDatoMapper = tipoDatoMapper;
        this.tipoDatoSearchRepository = tipoDatoSearchRepository;
    }

    /**
     * Save a tipoDato.
     *
     * @param tipoDatoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoDatoDTO save(TipoDatoDTO tipoDatoDTO) {
        log.debug("Request to save TipoDato : {}", tipoDatoDTO);
        TipoDato tipoDato = tipoDatoMapper.toEntity(tipoDatoDTO);
        tipoDato = tipoDatoRepository.save(tipoDato);
        TipoDatoDTO result = tipoDatoMapper.toDto(tipoDato);
        tipoDatoSearchRepository.save(tipoDato);
        return result;
    }

    /**
     * Get all the tipoDatoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoDatoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoDatoes");
        return tipoDatoRepository.findAll(pageable)
            .map(tipoDatoMapper::toDto);
    }


    /**
     * Get one tipoDato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoDatoDTO> findOne(Long id) {
        log.debug("Request to get TipoDato : {}", id);
        return tipoDatoRepository.findById(id)
            .map(tipoDatoMapper::toDto);
    }

    /**
     * Delete the tipoDato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TipoDato : {}", id);
        tipoDatoRepository.deleteById(id);
        tipoDatoSearchRepository.deleteById(id);
    }

    /**
     * Search for the tipoDato corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoDatoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoDatoes for query {}", query);
        return tipoDatoSearchRepository.search(queryStringQuery(query), pageable)
            .map(tipoDatoMapper::toDto);
    }
}
