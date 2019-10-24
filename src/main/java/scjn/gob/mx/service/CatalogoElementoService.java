package scjn.gob.mx.service;

import scjn.gob.mx.domain.CatalogoElemento;
import scjn.gob.mx.repository.CatalogoElementoRepository;
import scjn.gob.mx.repository.search.CatalogoElementoSearchRepository;
import scjn.gob.mx.service.dto.CatalogoElementoDTO;
import scjn.gob.mx.service.mapper.CatalogoElementoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link CatalogoElemento}.
 */
@Service
@Transactional
public class CatalogoElementoService {

    private final Logger log = LoggerFactory.getLogger(CatalogoElementoService.class);

    private final CatalogoElementoRepository catalogoElementoRepository;

    private final CatalogoElementoMapper catalogoElementoMapper;

    private final CatalogoElementoSearchRepository catalogoElementoSearchRepository;

    public CatalogoElementoService(CatalogoElementoRepository catalogoElementoRepository, CatalogoElementoMapper catalogoElementoMapper, CatalogoElementoSearchRepository catalogoElementoSearchRepository) {
        this.catalogoElementoRepository = catalogoElementoRepository;
        this.catalogoElementoMapper = catalogoElementoMapper;
        this.catalogoElementoSearchRepository = catalogoElementoSearchRepository;
    }

    /**
     * Save a catalogoElemento.
     *
     * @param catalogoElementoDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogoElementoDTO save(CatalogoElementoDTO catalogoElementoDTO) {
        log.debug("Request to save CatalogoElemento : {}", catalogoElementoDTO);
        CatalogoElemento catalogoElemento = catalogoElementoMapper.toEntity(catalogoElementoDTO);
        catalogoElemento = catalogoElementoRepository.save(catalogoElemento);
        CatalogoElementoDTO result = catalogoElementoMapper.toDto(catalogoElemento);
        catalogoElementoSearchRepository.save(catalogoElemento);
        return result;
    }

    /**
     * Get all the catalogoElementos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogoElementoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogoElementos");
        return catalogoElementoRepository.findAll(pageable)
            .map(catalogoElementoMapper::toDto);
    }


    /**
     * Get one catalogoElemento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogoElementoDTO> findOne(Long id) {
        log.debug("Request to get CatalogoElemento : {}", id);
        return catalogoElementoRepository.findById(id)
            .map(catalogoElementoMapper::toDto);
    }

    /**
     * Delete the catalogoElemento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogoElemento : {}", id);
        catalogoElementoRepository.deleteById(id);
        catalogoElementoSearchRepository.deleteById(id);
    }

    /**
     * Search for the catalogoElemento corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogoElementoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CatalogoElementos for query {}", query);
        return catalogoElementoSearchRepository.search(queryStringQuery(query), pageable)
            .map(catalogoElementoMapper::toDto);
    }
}
