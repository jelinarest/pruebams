package scjn.gob.mx.service;

import scjn.gob.mx.domain.Plantilla;
import scjn.gob.mx.repository.PlantillaRepository;
import scjn.gob.mx.repository.search.PlantillaSearchRepository;
import scjn.gob.mx.service.dto.PlantillaDTO;
import scjn.gob.mx.service.mapper.PlantillaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Plantilla}.
 */
@Service
@Transactional
public class PlantillaService {

    private final Logger log = LoggerFactory.getLogger(PlantillaService.class);

    private final PlantillaRepository plantillaRepository;

    private final PlantillaMapper plantillaMapper;

    private final PlantillaSearchRepository plantillaSearchRepository;

    public PlantillaService(PlantillaRepository plantillaRepository, PlantillaMapper plantillaMapper, PlantillaSearchRepository plantillaSearchRepository) {
        this.plantillaRepository = plantillaRepository;
        this.plantillaMapper = plantillaMapper;
        this.plantillaSearchRepository = plantillaSearchRepository;
    }

    /**
     * Save a plantilla.
     *
     * @param plantillaDTO the entity to save.
     * @return the persisted entity.
     */
    public PlantillaDTO save(PlantillaDTO plantillaDTO) {
        log.debug("Request to save Plantilla : {}", plantillaDTO);
        Plantilla plantilla = plantillaMapper.toEntity(plantillaDTO);
        plantilla = plantillaRepository.save(plantilla);
        PlantillaDTO result = plantillaMapper.toDto(plantilla);
        plantillaSearchRepository.save(plantilla);
        return result;
    }

    /**
     * Get all the plantillas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlantillaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Plantillas");
        return plantillaRepository.findAll(pageable)
            .map(plantillaMapper::toDto);
    }


    /**
     * Get one plantilla by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlantillaDTO> findOne(Long id) {
        log.debug("Request to get Plantilla : {}", id);
        return plantillaRepository.findById(id)
            .map(plantillaMapper::toDto);
    }

    /**
     * Delete the plantilla by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Plantilla : {}", id);
        plantillaRepository.deleteById(id);
        plantillaSearchRepository.deleteById(id);
    }

    /**
     * Search for the plantilla corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlantillaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Plantillas for query {}", query);
        return plantillaSearchRepository.search(queryStringQuery(query), pageable)
            .map(plantillaMapper::toDto);
    }
}
