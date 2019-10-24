package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.TipoValidacionService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.TipoValidacionDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.TipoValidacion}.
 */
@RestController
@RequestMapping("/api")
public class TipoValidacionResource {

    private final Logger log = LoggerFactory.getLogger(TipoValidacionResource.class);

    private static final String ENTITY_NAME = "pruebamsTipoValidacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoValidacionService tipoValidacionService;

    public TipoValidacionResource(TipoValidacionService tipoValidacionService) {
        this.tipoValidacionService = tipoValidacionService;
    }

    /**
     * {@code POST  /tipo-validacions} : Create a new tipoValidacion.
     *
     * @param tipoValidacionDTO the tipoValidacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoValidacionDTO, or with status {@code 400 (Bad Request)} if the tipoValidacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-validacions")
    public ResponseEntity<TipoValidacionDTO> createTipoValidacion(@Valid @RequestBody TipoValidacionDTO tipoValidacionDTO) throws URISyntaxException {
        log.debug("REST request to save TipoValidacion : {}", tipoValidacionDTO);
        if (tipoValidacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoValidacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoValidacionDTO result = tipoValidacionService.save(tipoValidacionDTO);
        return ResponseEntity.created(new URI("/api/tipo-validacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-validacions} : Updates an existing tipoValidacion.
     *
     * @param tipoValidacionDTO the tipoValidacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoValidacionDTO,
     * or with status {@code 400 (Bad Request)} if the tipoValidacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoValidacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-validacions")
    public ResponseEntity<TipoValidacionDTO> updateTipoValidacion(@Valid @RequestBody TipoValidacionDTO tipoValidacionDTO) throws URISyntaxException {
        log.debug("REST request to update TipoValidacion : {}", tipoValidacionDTO);
        if (tipoValidacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoValidacionDTO result = tipoValidacionService.save(tipoValidacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoValidacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-validacions} : get all the tipoValidacions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoValidacions in body.
     */
    @GetMapping("/tipo-validacions")
    public ResponseEntity<List<TipoValidacionDTO>> getAllTipoValidacions(Pageable pageable) {
        log.debug("REST request to get a page of TipoValidacions");
        Page<TipoValidacionDTO> page = tipoValidacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-validacions/:id} : get the "id" tipoValidacion.
     *
     * @param id the id of the tipoValidacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoValidacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-validacions/{id}")
    public ResponseEntity<TipoValidacionDTO> getTipoValidacion(@PathVariable Long id) {
        log.debug("REST request to get TipoValidacion : {}", id);
        Optional<TipoValidacionDTO> tipoValidacionDTO = tipoValidacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoValidacionDTO);
    }

    /**
     * {@code DELETE  /tipo-validacions/:id} : delete the "id" tipoValidacion.
     *
     * @param id the id of the tipoValidacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-validacions/{id}")
    public ResponseEntity<Void> deleteTipoValidacion(@PathVariable Long id) {
        log.debug("REST request to delete TipoValidacion : {}", id);
        tipoValidacionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/tipo-validacions?query=:query} : search for the tipoValidacion corresponding
     * to the query.
     *
     * @param query the query of the tipoValidacion search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/tipo-validacions")
    public ResponseEntity<List<TipoValidacionDTO>> searchTipoValidacions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TipoValidacions for query {}", query);
        Page<TipoValidacionDTO> page = tipoValidacionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
