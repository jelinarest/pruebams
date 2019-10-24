package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.TipoPlantilla;
import scjn.gob.mx.repository.TipoPlantillaRepository;
import scjn.gob.mx.repository.search.TipoPlantillaSearchRepository;
import scjn.gob.mx.service.TipoPlantillaService;
import scjn.gob.mx.service.dto.TipoPlantillaDTO;
import scjn.gob.mx.service.mapper.TipoPlantillaMapper;
import scjn.gob.mx.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static scjn.gob.mx.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TipoPlantillaResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class TipoPlantillaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private TipoPlantillaRepository tipoPlantillaRepository;

    @Autowired
    private TipoPlantillaMapper tipoPlantillaMapper;

    @Autowired
    private TipoPlantillaService tipoPlantillaService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.TipoPlantillaSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoPlantillaSearchRepository mockTipoPlantillaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTipoPlantillaMockMvc;

    private TipoPlantilla tipoPlantilla;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoPlantillaResource tipoPlantillaResource = new TipoPlantillaResource(tipoPlantillaService);
        this.restTipoPlantillaMockMvc = MockMvcBuilders.standaloneSetup(tipoPlantillaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoPlantilla createEntity(EntityManager em) {
        TipoPlantilla tipoPlantilla = new TipoPlantilla()
            .nombre(DEFAULT_NOMBRE);
        return tipoPlantilla;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoPlantilla createUpdatedEntity(EntityManager em) {
        TipoPlantilla tipoPlantilla = new TipoPlantilla()
            .nombre(UPDATED_NOMBRE);
        return tipoPlantilla;
    }

    @BeforeEach
    public void initTest() {
        tipoPlantilla = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoPlantilla() throws Exception {
        int databaseSizeBeforeCreate = tipoPlantillaRepository.findAll().size();

        // Create the TipoPlantilla
        TipoPlantillaDTO tipoPlantillaDTO = tipoPlantillaMapper.toDto(tipoPlantilla);
        restTipoPlantillaMockMvc.perform(post("/api/tipo-plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPlantillaDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoPlantilla in the database
        List<TipoPlantilla> tipoPlantillaList = tipoPlantillaRepository.findAll();
        assertThat(tipoPlantillaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoPlantilla testTipoPlantilla = tipoPlantillaList.get(tipoPlantillaList.size() - 1);
        assertThat(testTipoPlantilla.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the TipoPlantilla in Elasticsearch
        verify(mockTipoPlantillaSearchRepository, times(1)).save(testTipoPlantilla);
    }

    @Test
    @Transactional
    public void createTipoPlantillaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoPlantillaRepository.findAll().size();

        // Create the TipoPlantilla with an existing ID
        tipoPlantilla.setId(1L);
        TipoPlantillaDTO tipoPlantillaDTO = tipoPlantillaMapper.toDto(tipoPlantilla);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoPlantillaMockMvc.perform(post("/api/tipo-plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPlantillaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoPlantilla in the database
        List<TipoPlantilla> tipoPlantillaList = tipoPlantillaRepository.findAll();
        assertThat(tipoPlantillaList).hasSize(databaseSizeBeforeCreate);

        // Validate the TipoPlantilla in Elasticsearch
        verify(mockTipoPlantillaSearchRepository, times(0)).save(tipoPlantilla);
    }


    @Test
    @Transactional
    public void getAllTipoPlantillas() throws Exception {
        // Initialize the database
        tipoPlantillaRepository.saveAndFlush(tipoPlantilla);

        // Get all the tipoPlantillaList
        restTipoPlantillaMockMvc.perform(get("/api/tipo-plantillas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoPlantilla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getTipoPlantilla() throws Exception {
        // Initialize the database
        tipoPlantillaRepository.saveAndFlush(tipoPlantilla);

        // Get the tipoPlantilla
        restTipoPlantillaMockMvc.perform(get("/api/tipo-plantillas/{id}", tipoPlantilla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoPlantilla.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    public void getNonExistingTipoPlantilla() throws Exception {
        // Get the tipoPlantilla
        restTipoPlantillaMockMvc.perform(get("/api/tipo-plantillas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoPlantilla() throws Exception {
        // Initialize the database
        tipoPlantillaRepository.saveAndFlush(tipoPlantilla);

        int databaseSizeBeforeUpdate = tipoPlantillaRepository.findAll().size();

        // Update the tipoPlantilla
        TipoPlantilla updatedTipoPlantilla = tipoPlantillaRepository.findById(tipoPlantilla.getId()).get();
        // Disconnect from session so that the updates on updatedTipoPlantilla are not directly saved in db
        em.detach(updatedTipoPlantilla);
        updatedTipoPlantilla
            .nombre(UPDATED_NOMBRE);
        TipoPlantillaDTO tipoPlantillaDTO = tipoPlantillaMapper.toDto(updatedTipoPlantilla);

        restTipoPlantillaMockMvc.perform(put("/api/tipo-plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPlantillaDTO)))
            .andExpect(status().isOk());

        // Validate the TipoPlantilla in the database
        List<TipoPlantilla> tipoPlantillaList = tipoPlantillaRepository.findAll();
        assertThat(tipoPlantillaList).hasSize(databaseSizeBeforeUpdate);
        TipoPlantilla testTipoPlantilla = tipoPlantillaList.get(tipoPlantillaList.size() - 1);
        assertThat(testTipoPlantilla.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the TipoPlantilla in Elasticsearch
        verify(mockTipoPlantillaSearchRepository, times(1)).save(testTipoPlantilla);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = tipoPlantillaRepository.findAll().size();

        // Create the TipoPlantilla
        TipoPlantillaDTO tipoPlantillaDTO = tipoPlantillaMapper.toDto(tipoPlantilla);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoPlantillaMockMvc.perform(put("/api/tipo-plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoPlantillaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoPlantilla in the database
        List<TipoPlantilla> tipoPlantillaList = tipoPlantillaRepository.findAll();
        assertThat(tipoPlantillaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoPlantilla in Elasticsearch
        verify(mockTipoPlantillaSearchRepository, times(0)).save(tipoPlantilla);
    }

    @Test
    @Transactional
    public void deleteTipoPlantilla() throws Exception {
        // Initialize the database
        tipoPlantillaRepository.saveAndFlush(tipoPlantilla);

        int databaseSizeBeforeDelete = tipoPlantillaRepository.findAll().size();

        // Delete the tipoPlantilla
        restTipoPlantillaMockMvc.perform(delete("/api/tipo-plantillas/{id}", tipoPlantilla.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoPlantilla> tipoPlantillaList = tipoPlantillaRepository.findAll();
        assertThat(tipoPlantillaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TipoPlantilla in Elasticsearch
        verify(mockTipoPlantillaSearchRepository, times(1)).deleteById(tipoPlantilla.getId());
    }

    @Test
    @Transactional
    public void searchTipoPlantilla() throws Exception {
        // Initialize the database
        tipoPlantillaRepository.saveAndFlush(tipoPlantilla);
        when(mockTipoPlantillaSearchRepository.search(queryStringQuery("id:" + tipoPlantilla.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipoPlantilla), PageRequest.of(0, 1), 1));
        // Search the tipoPlantilla
        restTipoPlantillaMockMvc.perform(get("/api/_search/tipo-plantillas?query=id:" + tipoPlantilla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoPlantilla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoPlantilla.class);
        TipoPlantilla tipoPlantilla1 = new TipoPlantilla();
        tipoPlantilla1.setId(1L);
        TipoPlantilla tipoPlantilla2 = new TipoPlantilla();
        tipoPlantilla2.setId(tipoPlantilla1.getId());
        assertThat(tipoPlantilla1).isEqualTo(tipoPlantilla2);
        tipoPlantilla2.setId(2L);
        assertThat(tipoPlantilla1).isNotEqualTo(tipoPlantilla2);
        tipoPlantilla1.setId(null);
        assertThat(tipoPlantilla1).isNotEqualTo(tipoPlantilla2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoPlantillaDTO.class);
        TipoPlantillaDTO tipoPlantillaDTO1 = new TipoPlantillaDTO();
        tipoPlantillaDTO1.setId(1L);
        TipoPlantillaDTO tipoPlantillaDTO2 = new TipoPlantillaDTO();
        assertThat(tipoPlantillaDTO1).isNotEqualTo(tipoPlantillaDTO2);
        tipoPlantillaDTO2.setId(tipoPlantillaDTO1.getId());
        assertThat(tipoPlantillaDTO1).isEqualTo(tipoPlantillaDTO2);
        tipoPlantillaDTO2.setId(2L);
        assertThat(tipoPlantillaDTO1).isNotEqualTo(tipoPlantillaDTO2);
        tipoPlantillaDTO1.setId(null);
        assertThat(tipoPlantillaDTO1).isNotEqualTo(tipoPlantillaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoPlantillaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoPlantillaMapper.fromId(null)).isNull();
    }
}
