package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.CronogramaEtapaService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.CronogramaEtapaDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.CronogramaEtapa}.
 */
@RestController
@RequestMapping("/api")
public class CronogramaEtapaResource {

    private final Logger log = LoggerFactory.getLogger(CronogramaEtapaResource.class);

    private static final String ENTITY_NAME = "pruebamsCronogramaEtapa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronogramaEtapaService cronogramaEtapaService;

    public CronogramaEtapaResource(CronogramaEtapaService cronogramaEtapaService) {
        this.cronogramaEtapaService = cronogramaEtapaService;
    }

    /**
     * {@code POST  /cronograma-etapas} : Create a new cronogramaEtapa.
     *
     * @param cronogramaEtapaDTO the cronogramaEtapaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronogramaEtapaDTO, or with status {@code 400 (Bad Request)} if the cronogramaEtapa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cronograma-etapas")
    public ResponseEntity<CronogramaEtapaDTO> createCronogramaEtapa(@Valid @RequestBody CronogramaEtapaDTO cronogramaEtapaDTO) throws URISyntaxException {
        log.debug("REST request to save CronogramaEtapa : {}", cronogramaEtapaDTO);
        if (cronogramaEtapaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cronogramaEtapa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronogramaEtapaDTO result = cronogramaEtapaService.save(cronogramaEtapaDTO);
        return ResponseEntity.created(new URI("/api/cronograma-etapas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cronograma-etapas} : Updates an existing cronogramaEtapa.
     *
     * @param cronogramaEtapaDTO the cronogramaEtapaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronogramaEtapaDTO,
     * or with status {@code 400 (Bad Request)} if the cronogramaEtapaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronogramaEtapaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronograma-etapas")
    public ResponseEntity<CronogramaEtapaDTO> updateCronogramaEtapa(@Valid @RequestBody CronogramaEtapaDTO cronogramaEtapaDTO) throws URISyntaxException {
        log.debug("REST request to update CronogramaEtapa : {}", cronogramaEtapaDTO);
        if (cronogramaEtapaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CronogramaEtapaDTO result = cronogramaEtapaService.save(cronogramaEtapaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronogramaEtapaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cronograma-etapas} : get all the cronogramaEtapas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronogramaEtapas in body.
     */
    @GetMapping("/cronograma-etapas")
    public ResponseEntity<List<CronogramaEtapaDTO>> getAllCronogramaEtapas(Pageable pageable) {
        log.debug("REST request to get a page of CronogramaEtapas");
        Page<CronogramaEtapaDTO> page = cronogramaEtapaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cronograma-etapas/:id} : get the "id" cronogramaEtapa.
     *
     * @param id the id of the cronogramaEtapaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronogramaEtapaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cronograma-etapas/{id}")
    public ResponseEntity<CronogramaEtapaDTO> getCronogramaEtapa(@PathVariable Long id) {
        log.debug("REST request to get CronogramaEtapa : {}", id);
        Optional<CronogramaEtapaDTO> cronogramaEtapaDTO = cronogramaEtapaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronogramaEtapaDTO);
    }

    /**
     * {@code DELETE  /cronograma-etapas/:id} : delete the "id" cronogramaEtapa.
     *
     * @param id the id of the cronogramaEtapaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cronograma-etapas/{id}")
    public ResponseEntity<Void> deleteCronogramaEtapa(@PathVariable Long id) {
        log.debug("REST request to delete CronogramaEtapa : {}", id);
        cronogramaEtapaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cronograma-etapas?query=:query} : search for the cronogramaEtapa corresponding
     * to the query.
     *
     * @param query the query of the cronogramaEtapa search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cronograma-etapas")
    public ResponseEntity<List<CronogramaEtapaDTO>> searchCronogramaEtapas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CronogramaEtapas for query {}", query);
        Page<CronogramaEtapaDTO> page = cronogramaEtapaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
