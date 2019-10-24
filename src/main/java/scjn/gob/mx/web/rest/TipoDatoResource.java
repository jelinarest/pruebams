package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.TipoDatoService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.TipoDatoDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.TipoDato}.
 */
@RestController
@RequestMapping("/api")
public class TipoDatoResource {

    private final Logger log = LoggerFactory.getLogger(TipoDatoResource.class);

    private static final String ENTITY_NAME = "pruebamsTipoDato";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoDatoService tipoDatoService;

    public TipoDatoResource(TipoDatoService tipoDatoService) {
        this.tipoDatoService = tipoDatoService;
    }

    /**
     * {@code POST  /tipo-datoes} : Create a new tipoDato.
     *
     * @param tipoDatoDTO the tipoDatoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoDatoDTO, or with status {@code 400 (Bad Request)} if the tipoDato has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-datoes")
    public ResponseEntity<TipoDatoDTO> createTipoDato(@Valid @RequestBody TipoDatoDTO tipoDatoDTO) throws URISyntaxException {
        log.debug("REST request to save TipoDato : {}", tipoDatoDTO);
        if (tipoDatoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoDato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoDatoDTO result = tipoDatoService.save(tipoDatoDTO);
        return ResponseEntity.created(new URI("/api/tipo-datoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-datoes} : Updates an existing tipoDato.
     *
     * @param tipoDatoDTO the tipoDatoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoDatoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoDatoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoDatoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-datoes")
    public ResponseEntity<TipoDatoDTO> updateTipoDato(@Valid @RequestBody TipoDatoDTO tipoDatoDTO) throws URISyntaxException {
        log.debug("REST request to update TipoDato : {}", tipoDatoDTO);
        if (tipoDatoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoDatoDTO result = tipoDatoService.save(tipoDatoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tipoDatoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-datoes} : get all the tipoDatoes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoDatoes in body.
     */
    @GetMapping("/tipo-datoes")
    public ResponseEntity<List<TipoDatoDTO>> getAllTipoDatoes(Pageable pageable) {
        log.debug("REST request to get a page of TipoDatoes");
        Page<TipoDatoDTO> page = tipoDatoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-datoes/:id} : get the "id" tipoDato.
     *
     * @param id the id of the tipoDatoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoDatoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-datoes/{id}")
    public ResponseEntity<TipoDatoDTO> getTipoDato(@PathVariable Long id) {
        log.debug("REST request to get TipoDato : {}", id);
        Optional<TipoDatoDTO> tipoDatoDTO = tipoDatoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoDatoDTO);
    }

    /**
     * {@code DELETE  /tipo-datoes/:id} : delete the "id" tipoDato.
     *
     * @param id the id of the tipoDatoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-datoes/{id}")
    public ResponseEntity<Void> deleteTipoDato(@PathVariable Long id) {
        log.debug("REST request to delete TipoDato : {}", id);
        tipoDatoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/tipo-datoes?query=:query} : search for the tipoDato corresponding
     * to the query.
     *
     * @param query the query of the tipoDato search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/tipo-datoes")
    public ResponseEntity<List<TipoDatoDTO>> searchTipoDatoes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TipoDatoes for query {}", query);
        Page<TipoDatoDTO> page = tipoDatoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
