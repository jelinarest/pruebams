package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.CampoService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.CampoDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.Campo}.
 */
@RestController
@RequestMapping("/api")
public class CampoResource {

    private final Logger log = LoggerFactory.getLogger(CampoResource.class);

    private static final String ENTITY_NAME = "pruebamsCampo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampoService campoService;

    public CampoResource(CampoService campoService) {
        this.campoService = campoService;
    }

    /**
     * {@code POST  /campos} : Create a new campo.
     *
     * @param campoDTO the campoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campoDTO, or with status {@code 400 (Bad Request)} if the campo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campos")
    public ResponseEntity<CampoDTO> createCampo(@Valid @RequestBody CampoDTO campoDTO) throws URISyntaxException {
        log.debug("REST request to save Campo : {}", campoDTO);
        if (campoDTO.getId() != null) {
            throw new BadRequestAlertException("A new campo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CampoDTO result = campoService.save(campoDTO);
        return ResponseEntity.created(new URI("/api/campos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campos} : Updates an existing campo.
     *
     * @param campoDTO the campoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campoDTO,
     * or with status {@code 400 (Bad Request)} if the campoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campos")
    public ResponseEntity<CampoDTO> updateCampo(@Valid @RequestBody CampoDTO campoDTO) throws URISyntaxException {
        log.debug("REST request to update Campo : {}", campoDTO);
        if (campoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CampoDTO result = campoService.save(campoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, campoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /campos} : get all the campos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campos in body.
     */
    @GetMapping("/campos")
    public ResponseEntity<List<CampoDTO>> getAllCampos(Pageable pageable) {
        log.debug("REST request to get a page of Campos");
        Page<CampoDTO> page = campoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /campos/:id} : get the "id" campo.
     *
     * @param id the id of the campoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campos/{id}")
    public ResponseEntity<CampoDTO> getCampo(@PathVariable Long id) {
        log.debug("REST request to get Campo : {}", id);
        Optional<CampoDTO> campoDTO = campoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(campoDTO);
    }

    /**
     * {@code DELETE  /campos/:id} : delete the "id" campo.
     *
     * @param id the id of the campoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campos/{id}")
    public ResponseEntity<Void> deleteCampo(@PathVariable Long id) {
        log.debug("REST request to delete Campo : {}", id);
        campoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/campos?query=:query} : search for the campo corresponding
     * to the query.
     *
     * @param query the query of the campo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/campos")
    public ResponseEntity<List<CampoDTO>> searchCampos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Campos for query {}", query);
        Page<CampoDTO> page = campoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
