package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.CatalogoElementoService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.CatalogoElementoDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.CatalogoElemento}.
 */
@RestController
@RequestMapping("/api")
public class CatalogoElementoResource {

    private final Logger log = LoggerFactory.getLogger(CatalogoElementoResource.class);

    private static final String ENTITY_NAME = "pruebamsCatalogoElemento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogoElementoService catalogoElementoService;

    public CatalogoElementoResource(CatalogoElementoService catalogoElementoService) {
        this.catalogoElementoService = catalogoElementoService;
    }

    /**
     * {@code POST  /catalogo-elementos} : Create a new catalogoElemento.
     *
     * @param catalogoElementoDTO the catalogoElementoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogoElementoDTO, or with status {@code 400 (Bad Request)} if the catalogoElemento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalogo-elementos")
    public ResponseEntity<CatalogoElementoDTO> createCatalogoElemento(@Valid @RequestBody CatalogoElementoDTO catalogoElementoDTO) throws URISyntaxException {
        log.debug("REST request to save CatalogoElemento : {}", catalogoElementoDTO);
        if (catalogoElementoDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogoElemento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogoElementoDTO result = catalogoElementoService.save(catalogoElementoDTO);
        return ResponseEntity.created(new URI("/api/catalogo-elementos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalogo-elementos} : Updates an existing catalogoElemento.
     *
     * @param catalogoElementoDTO the catalogoElementoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogoElementoDTO,
     * or with status {@code 400 (Bad Request)} if the catalogoElementoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogoElementoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalogo-elementos")
    public ResponseEntity<CatalogoElementoDTO> updateCatalogoElemento(@Valid @RequestBody CatalogoElementoDTO catalogoElementoDTO) throws URISyntaxException {
        log.debug("REST request to update CatalogoElemento : {}", catalogoElementoDTO);
        if (catalogoElementoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CatalogoElementoDTO result = catalogoElementoService.save(catalogoElementoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, catalogoElementoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /catalogo-elementos} : get all the catalogoElementos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogoElementos in body.
     */
    @GetMapping("/catalogo-elementos")
    public ResponseEntity<List<CatalogoElementoDTO>> getAllCatalogoElementos(Pageable pageable) {
        log.debug("REST request to get a page of CatalogoElementos");
        Page<CatalogoElementoDTO> page = catalogoElementoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalogo-elementos/:id} : get the "id" catalogoElemento.
     *
     * @param id the id of the catalogoElementoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogoElementoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalogo-elementos/{id}")
    public ResponseEntity<CatalogoElementoDTO> getCatalogoElemento(@PathVariable Long id) {
        log.debug("REST request to get CatalogoElemento : {}", id);
        Optional<CatalogoElementoDTO> catalogoElementoDTO = catalogoElementoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogoElementoDTO);
    }

    /**
     * {@code DELETE  /catalogo-elementos/:id} : delete the "id" catalogoElemento.
     *
     * @param id the id of the catalogoElementoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalogo-elementos/{id}")
    public ResponseEntity<Void> deleteCatalogoElemento(@PathVariable Long id) {
        log.debug("REST request to delete CatalogoElemento : {}", id);
        catalogoElementoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/catalogo-elementos?query=:query} : search for the catalogoElemento corresponding
     * to the query.
     *
     * @param query the query of the catalogoElemento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/catalogo-elementos")
    public ResponseEntity<List<CatalogoElementoDTO>> searchCatalogoElementos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CatalogoElementos for query {}", query);
        Page<CatalogoElementoDTO> page = catalogoElementoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
