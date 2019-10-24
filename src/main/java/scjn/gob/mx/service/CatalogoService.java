package scjn.gob.mx.service;

import scjn.gob.mx.domain.Catalogo;
import scjn.gob.mx.repository.CatalogoRepository;
import scjn.gob.mx.repository.search.CatalogoSearchRepository;
import scjn.gob.mx.service.dto.CatalogoDTO;
import scjn.gob.mx.service.mapper.CatalogoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Catalogo}.
 */
@Service
@Transactional
public class CatalogoService {

    private final Logger log = LoggerFactory.getLogger(CatalogoService.class);

    private final CatalogoRepository catalogoRepository;

    private final CatalogoMapper catalogoMapper;

    private final CatalogoSearchRepository catalogoSearchRepository;

    public CatalogoService(CatalogoRepository catalogoRepository, CatalogoMapper catalogoMapper, CatalogoSearchRepository catalogoSearchRepository) {
        this.catalogoRepository = catalogoRepository;
        this.catalogoMapper = catalogoMapper;
        this.catalogoSearchRepository = catalogoSearchRepository;
    }

    /**
     * Save a catalogo.
     *
     * @param catalogoDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogoDTO save(CatalogoDTO catalogoDTO) {
        log.debug("Request to save Catalogo : {}", catalogoDTO);
        Catalogo catalogo = catalogoMapper.toEntity(catalogoDTO);
        catalogo = catalogoRepository.save(catalogo);
        CatalogoDTO result = catalogoMapper.toDto(catalogo);
        catalogoSearchRepository.save(catalogo);
        return result;
    }

    /**
     * Get all the catalogos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Catalogos");
        return catalogoRepository.findAll(pageable)
            .map(catalogoMapper::toDto);
    }


    /**
     * Get one catalogo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogoDTO> findOne(Long id) {
        log.debug("Request to get Catalogo : {}", id);
        return catalogoRepository.findById(id)
            .map(catalogoMapper::toDto);
    }

    /**
     * Delete the catalogo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Catalogo : {}", id);
        catalogoRepository.deleteById(id);
        catalogoSearchRepository.deleteById(id);
    }

    /**
     * Search for the catalogo corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Catalogos for query {}", query);
        return catalogoSearchRepository.search(queryStringQuery(query), pageable)
            .map(catalogoMapper::toDto);
    }
}
