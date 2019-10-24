package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.TipoPlantillaService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.TipoPlantillaDTO;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link scjn.gob.mx.domain.TipoPlantilla}.
 */
@RestController
@RequestMapping("/api")
public class TipoPlantillaResource {

    private final Logger log = LoggerFactory.getLogger(TipoPlantillaResource.class);

    private static final String ENTITY_NAME = "pruebamsTipoPlantilla";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoPlantillaService tipoPlantillaService;

    public TipoPlantillaResource(TipoPlantillaService tipoPlantillaService) {
        this.tipoPlantillaService = tipoPlantillaService;
    }

    /**
     * {@code POST  /tipo-plantillas} : Create a new tipoPlantilla.
     *
     * @param tipoPlantillaDTO the tipoPlantillaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoPlantillaDTO, or with status {@code 400 (Bad Request)} if the tipoPlantilla has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-plantillas")
    public ResponseEntity<TipoPlantillaDTO> createTipoPlantilla(@RequestBody TipoPlantillaDTO tipoPlantillaDTO) throws URISyntaxException {
        log.debug("REST request to save TipoPlantilla : {}", tipoPlantillaDTO);
        if (tipoPlantillaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoPlantilla cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoPlantillaDTO result = tipoPlantillaService.save(tipoPlantillaDTO);
        return ResponseEntity.created(new URI("/api/tipo-plantillas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-plantillas} : Updates an existing tipoPlantilla.
     *
     * @param tipoPlantillaDTO the tipoPlantillaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoPlantillaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoPlantillaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoPlantillaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-plantillas")
    public ResponseEntity<TipoPlantillaDTO> updateTipoPlantilla(@RequestBody TipoPlantillaDTO tipoPlantillaDTO) throws URISyntaxException {
        log.debug("REST request to update TipoPlantilla : {}", tipoPlantillaDTO);
        if (tipoPlantillaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoPlantillaDTO result = tipoPlantillaService.save(tipoPlantillaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoPlantillaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-plantillas} : get all the tipoPlantillas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoPlantillas in body.
     */
    @GetMapping("/tipo-plantillas")
    public ResponseEntity<List<TipoPlantillaDTO>> getAllTipoPlantillas(Pageable pageable) {
        log.debug("REST request to get a page of TipoPlantillas");
        Page<TipoPlantillaDTO> page = tipoPlantillaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-plantillas/:id} : get the "id" tipoPlantilla.
     *
     * @param id the id of the tipoPlantillaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoPlantillaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-plantillas/{id}")
    public ResponseEntity<TipoPlantillaDTO> getTipoPlantilla(@PathVariable Long id) {
        log.debug("REST request to get TipoPlantilla : {}", id);
        Optional<TipoPlantillaDTO> tipoPlantillaDTO = tipoPlantillaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoPlantillaDTO);
    }

    /**
     * {@code DELETE  /tipo-plantillas/:id} : delete the "id" tipoPlantilla.
     *
     * @param id the id of the tipoPlantillaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-plantillas/{id}")
    public ResponseEntity<Void> deleteTipoPlantilla(@PathVariable Long id) {
        log.debug("REST request to delete TipoPlantilla : {}", id);
        tipoPlantillaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/tipo-plantillas?query=:query} : search for the tipoPlantilla corresponding
     * to the query.
     *
     * @param query the query of the tipoPlantilla search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/tipo-plantillas")
    public ResponseEntity<List<TipoPlantillaDTO>> searchTipoPlantillas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TipoPlantillas for query {}", query);
        Page<TipoPlantillaDTO> page = tipoPlantillaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
