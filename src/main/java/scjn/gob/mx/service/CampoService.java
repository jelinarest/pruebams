package scjn.gob.mx.service;

import scjn.gob.mx.domain.Campo;
import scjn.gob.mx.repository.CampoRepository;
import scjn.gob.mx.repository.search.CampoSearchRepository;
import scjn.gob.mx.service.dto.CampoDTO;
import scjn.gob.mx.service.mapper.CampoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Campo}.
 */
@Service
@Transactional
public class CampoService {

    private final Logger log = LoggerFactory.getLogger(CampoService.class);

    private final CampoRepository campoRepository;

    private final CampoMapper campoMapper;

    private final CampoSearchRepository campoSearchRepository;

    public CampoService(CampoRepository campoRepository, CampoMapper campoMapper, CampoSearchRepository campoSearchRepository) {
        this.campoRepository = campoRepository;
        this.campoMapper = campoMapper;
        this.campoSearchRepository = campoSearchRepository;
    }

    /**
     * Save a campo.
     *
     * @param campoDTO the entity to save.
     * @return the persisted entity.
     */
    public CampoDTO save(CampoDTO campoDTO) {
        log.debug("Request to save Campo : {}", campoDTO);
        Campo campo = campoMapper.toEntity(campoDTO);
        campo = campoRepository.save(campo);
        CampoDTO result = campoMapper.toDto(campo);
        campoSearchRepository.save(campo);
        return result;
    }

    /**
     * Get all the campos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CampoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campos");
        return campoRepository.findAll(pageable)
            .map(campoMapper::toDto);
    }


    /**
     * Get one campo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CampoDTO> findOne(Long id) {
        log.debug("Request to get Campo : {}", id);
        return campoRepository.findById(id)
            .map(campoMapper::toDto);
    }

    /**
     * Delete the campo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Campo : {}", id);
        campoRepository.deleteById(id);
        campoSearchRepository.deleteById(id);
    }

    /**
     * Search for the campo corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CampoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Campos for query {}", query);
        return campoSearchRepository.search(queryStringQuery(query), pageable)
            .map(campoMapper::toDto);
    }
}
