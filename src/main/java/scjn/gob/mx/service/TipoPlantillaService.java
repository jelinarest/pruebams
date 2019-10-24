package scjn.gob.mx.service;

import scjn.gob.mx.domain.TipoPlantilla;
import scjn.gob.mx.repository.TipoPlantillaRepository;
import scjn.gob.mx.repository.search.TipoPlantillaSearchRepository;
import scjn.gob.mx.service.dto.TipoPlantillaDTO;
import scjn.gob.mx.service.mapper.TipoPlantillaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TipoPlantilla}.
 */
@Service
@Transactional
public class TipoPlantillaService {

    private final Logger log = LoggerFactory.getLogger(TipoPlantillaService.class);

    private final TipoPlantillaRepository tipoPlantillaRepository;

    private final TipoPlantillaMapper tipoPlantillaMapper;

    private final TipoPlantillaSearchRepository tipoPlantillaSearchRepository;

    public TipoPlantillaService(TipoPlantillaRepository tipoPlantillaRepository, TipoPlantillaMapper tipoPlantillaMapper, TipoPlantillaSearchRepository tipoPlantillaSearchRepository) {
        this.tipoPlantillaRepository = tipoPlantillaRepository;
        this.tipoPlantillaMapper = tipoPlantillaMapper;
        this.tipoPlantillaSearchRepository = tipoPlantillaSearchRepository;
    }

    /**
     * Save a tipoPlantilla.
     *
     * @param tipoPlantillaDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoPlantillaDTO save(TipoPlantillaDTO tipoPlantillaDTO) {
        log.debug("Request to save TipoPlantilla : {}", tipoPlantillaDTO);
        TipoPlantilla tipoPlantilla = tipoPlantillaMapper.toEntity(tipoPlantillaDTO);
        tipoPlantilla = tipoPlantillaRepository.save(tipoPlantilla);
        TipoPlantillaDTO result = tipoPlantillaMapper.toDto(tipoPlantilla);
        tipoPlantillaSearchRepository.save(tipoPlantilla);
        return result;
    }

    /**
     * Get all the tipoPlantillas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoPlantillaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoPlantillas");
        return tipoPlantillaRepository.findAll(pageable)
            .map(tipoPlantillaMapper::toDto);
    }


    /**
     * Get one tipoPlantilla by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoPlantillaDTO> findOne(Long id) {
        log.debug("Request to get TipoPlantilla : {}", id);
        return tipoPlantillaRepository.findById(id)
            .map(tipoPlantillaMapper::toDto);
    }

    /**
     * Delete the tipoPlantilla by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TipoPlantilla : {}", id);
        tipoPlantillaRepository.deleteById(id);
        tipoPlantillaSearchRepository.deleteById(id);
    }

    /**
     * Search for the tipoPlantilla corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoPlantillaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TipoPlantillas for query {}", query);
        return tipoPlantillaSearchRepository.search(queryStringQuery(query), pageable)
            .map(tipoPlantillaMapper::toDto);
    }
}
