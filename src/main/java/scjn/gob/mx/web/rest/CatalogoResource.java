package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.CatalogoService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.CatalogoDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link scjn.gob.mx.domain.Catalogo}.
 */
@RestController
@RequestMapping("/api")
public class CatalogoResource {

    private final Logger log = LoggerFactory.getLogger(CatalogoResource.class);

    private static final String ENTITY_NAME = "pruebamsCatalogo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoService catalogoService;

    public CatalogoResource(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    /**
     * {@code POST  /catalogos} : Create a new catalogo.
     *
     * @param catalogoDTO the catalogoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogoDTO, or with status {@code 400 (Bad Request)} if the catalogo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalogos")
    public ResponseEntity<CatalogoDTO> createCatalogo(@Valid @RequestBody CatalogoDTO catalogoDTO) throws URISyntaxException {
        log.debug("REST request to save Catalogo : {}", catalogoDTO);
        if (catalogoDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogoDTO result = catalogoService.save(catalogoDTO);
        return ResponseEntity.created(new URI("/api/catalogos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalogos} : Updates an existing catalogo.
     *
     * @param catalogoDTO the catalogoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoDTO,
     * or with status {@code 400 (Bad Request)} if the catalogoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalogos")
    public ResponseEntity<CatalogoDTO> updateCatalogo(@Valid @RequestBody CatalogoDTO catalogoDTO) throws URISyntaxException {
        log.debug("REST request to update Catalogo : {}", catalogoDTO);
        if (catalogoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CatalogoDTO result = catalogoService.save(catalogoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, catalogoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /catalogos} : get all the catalogos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogos in body.
     */
    @GetMapping("/catalogos")
    public ResponseEntity<List<CatalogoDTO>> getAllCatalogos(Pageable pageable) {
        log.debug("REST request to get a page of Catalogos");
        Page<CatalogoDTO> page = catalogoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalogos/:id} : get the "id" catalogo.
     *
     * @param id the id of the catalogoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalogos/{id}")
    public ResponseEntity<CatalogoDTO> getCatalogo(@PathVariable Long id) {
        log.debug("REST request to get Catalogo : {}", id);
        Optional<CatalogoDTO> catalogoDTO = catalogoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogoDTO);
    }

    /**
     * {@code DELETE  /catalogos/:id} : delete the "id" catalogo.
     *
     * @param id the id of the catalogoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalogos/{id}")
    public ResponseEntity<Void> deleteCatalogo(@PathVariable Long id) {
        log.debug("REST request to delete Catalogo : {}", id);
        catalogoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/catalogos?query=:query} : search for the catalogo corresponding
     * to the query.
     *
     * @param query the query of the catalogo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/catalogos")
    public ResponseEntity<List<CatalogoDTO>> searchCatalogos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Catalogos for query {}", query);
        Page<CatalogoDTO> page = catalogoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
