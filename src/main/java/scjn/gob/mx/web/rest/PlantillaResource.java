package scjn.gob.mx.web.rest;

import scjn.gob.mx.service.PlantillaService;
import scjn.gob.mx.web.rest.errors.BadRequestAlertException;
import scjn.gob.mx.service.dto.PlantillaDTO;

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
 * REST controller for managing {@link scjn.gob.mx.domain.Plantilla}.
 */
@RestController
@RequestMapping("/api")
public class PlantillaResource {

    private final Logger log = LoggerFactory.getLogger(PlantillaResource.class);

    private static final String ENTITY_NAME = "pruebamsPlantilla";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantillaService plantillaService;

    public PlantillaResource(PlantillaService plantillaService) {
        this.plantillaService = plantillaService;
    }

    /**
     * {@code POST  /plantillas} : Create a new plantilla.
     *
     * @param plantillaDTO the plantillaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plantillaDTO, or with status {@code 400 (Bad Request)} if the plantilla has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plantillas")
    public ResponseEntity<PlantillaDTO> createPlantilla(@Valid @RequestBody PlantillaDTO plantillaDTO) throws URISyntaxException {
        log.debug("REST request to save Plantilla : {}", plantillaDTO);
        if (plantillaDTO.getId() != null) {
            throw new BadRequestAlertException("A new plantilla cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlantillaDTO result = plantillaService.save(plantillaDTO);
        return ResponseEntity.created(new URI("/api/plantillas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plantillas} : Updates an existing plantilla.
     *
     * @param plantillaDTO the plantillaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantillaDTO,
     * or with status {@code 400 (Bad Request)} if the plantillaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plantillaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plantillas")
    public ResponseEntity<PlantillaDTO> updatePlantilla(@Valid @RequestBody PlantillaDTO plantillaDTO) throws URISyntaxException {
        log.debug("REST request to update Plantilla : {}", plantillaDTO);
        if (plantillaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlantillaDTO result = plantillaService.save(plantillaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plantillaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plantillas} : get all the plantillas.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantillas in body.
     */
    @GetMapping("/plantillas")
    public ResponseEntity<List<PlantillaDTO>> getAllPlantillas(Pageable pageable) {
        log.debug("REST request to get a page of Plantillas");
        Page<PlantillaDTO> page = plantillaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plantillas/:id} : get the "id" plantilla.
     *
     * @param id the id of the plantillaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plantillaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plantillas/{id}")
    public ResponseEntity<PlantillaDTO> getPlantilla(@PathVariable Long id) {
        log.debug("REST request to get Plantilla : {}", id);
        Optional<PlantillaDTO> plantillaDTO = plantillaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plantillaDTO);
    }

    /**
     * {@code DELETE  /plantillas/:id} : delete the "id" plantilla.
     *
     * @param id the id of the plantillaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plantillas/{id}")
    public ResponseEntity<Void> deletePlantilla(@PathVariable Long id) {
        log.debug("REST request to delete Plantilla : {}", id);
        plantillaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/plantillas?query=:query} : search for the plantilla corresponding
     * to the query.
     *
     * @param query the query of the plantilla search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/plantillas")
    public ResponseEntity<List<PlantillaDTO>> searchPlantillas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Plantillas for query {}", query);
        Page<PlantillaDTO> page = plantillaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
