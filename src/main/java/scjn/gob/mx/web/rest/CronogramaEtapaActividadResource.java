package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.CronogramaEtapaActividadService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.CronogramaEtapaActividadDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.CronogramaEtapaActividad}.
 */
@RestController
@RequestMapping("/api")
public class CronogramaEtapaActividadResource {

    private final Logger log = LoggerFactory.getLogger(CronogramaEtapaActividadResource.class);

    private static final String ENTITY_NAME = "pruebamsCronogramaEtapaActividad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronogramaEtapaActividadService cronogramaEtapaActividadService;

    public CronogramaEtapaActividadResource(CronogramaEtapaActividadService cronogramaEtapaActividadService) {
        this.cronogramaEtapaActividadService = cronogramaEtapaActividadService;
    }

    /**
     * {@code POST  /cronograma-etapa-actividads} : Create a new cronogramaEtapaActividad.
     *
     * @param cronogramaEtapaActividadDTO the cronogramaEtapaActividadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronogramaEtapaActividadDTO, or with status {@code 400 (Bad Request)} if the cronogramaEtapaActividad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cronograma-etapa-actividads")
    public ResponseEntity<CronogramaEtapaActividadDTO> createCronogramaEtapaActividad(@Valid @RequestBody CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO) throws URISyntaxException {
        log.debug("REST request to save CronogramaEtapaActividad : {}", cronogramaEtapaActividadDTO);
        if (cronogramaEtapaActividadDTO.getId() != null) {
            throw new BadRequestAlertException("A new cronogramaEtapaActividad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronogramaEtapaActividadDTO result = cronogramaEtapaActividadService.save(cronogramaEtapaActividadDTO);
        return ResponseEntity.created(new URI("/api/cronograma-etapa-actividads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cronograma-etapa-actividads} : Updates an existing cronogramaEtapaActividad.
     *
     * @param cronogramaEtapaActividadDTO the cronogramaEtapaActividadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronogramaEtapaActividadDTO,
     * or with status {@code 400 (Bad Request)} if the cronogramaEtapaActividadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronogramaEtapaActividadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronograma-etapa-actividads")
    public ResponseEntity<CronogramaEtapaActividadDTO> updateCronogramaEtapaActividad(@Valid @RequestBody CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO) throws URISyntaxException {
        log.debug("REST request to update CronogramaEtapaActividad : {}", cronogramaEtapaActividadDTO);
        if (cronogramaEtapaActividadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CronogramaEtapaActividadDTO result = cronogramaEtapaActividadService.save(cronogramaEtapaActividadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronogramaEtapaActividadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cronograma-etapa-actividads} : get all the cronogramaEtapaActividads.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronogramaEtapaActividads in body.
     */
    @GetMapping("/cronograma-etapa-actividads")
    public ResponseEntity<List<CronogramaEtapaActividadDTO>> getAllCronogramaEtapaActividads(Pageable pageable) {
        log.debug("REST request to get a page of CronogramaEtapaActividads");
        Page<CronogramaEtapaActividadDTO> page = cronogramaEtapaActividadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cronograma-etapa-actividads/:id} : get the "id" cronogramaEtapaActividad.
     *
     * @param id the id of the cronogramaEtapaActividadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronogramaEtapaActividadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cronograma-etapa-actividads/{id}")
    public ResponseEntity<CronogramaEtapaActividadDTO> getCronogramaEtapaActividad(@PathVariable Long id) {
        log.debug("REST request to get CronogramaEtapaActividad : {}", id);
        Optional<CronogramaEtapaActividadDTO> cronogramaEtapaActividadDTO = cronogramaEtapaActividadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronogramaEtapaActividadDTO);
    }

    /**
     * {@code DELETE  /cronograma-etapa-actividads/:id} : delete the "id" cronogramaEtapaActividad.
     *
     * @param id the id of the cronogramaEtapaActividadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cronograma-etapa-actividads/{id}")
    public ResponseEntity<Void> deleteCronogramaEtapaActividad(@PathVariable Long id) {
        log.debug("REST request to delete CronogramaEtapaActividad : {}", id);
        cronogramaEtapaActividadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cronograma-etapa-actividads?query=:query} : search for the cronogramaEtapaActividad corresponding
     * to the query.
     *
     * @param query the query of the cronogramaEtapaActividad search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cronograma-etapa-actividads")
    public ResponseEntity<List<CronogramaEtapaActividadDTO>> searchCronogramaEtapaActividads(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CronogramaEtapaActividads for query {}", query);
        Page<CronogramaEtapaActividadDTO> page = cronogramaEtapaActividadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
