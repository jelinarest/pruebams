package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.TipoDato;
import scjn.gob.mx.repository.TipoDatoRepository;
import scjn.gob.mx.repository.search.TipoDatoSearchRepository;
import scjn.gob.mx.service.TipoDatoService;
import scjn.gob.mx.service.dto.TipoDatoDTO;
import scjn.gob.mx.service.mapper.TipoDatoMapper;
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
 * Integration tests for the {@link TipoDatoResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class TipoDatoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private TipoDatoRepository tipoDatoRepository;

    @Autowired
    private TipoDatoMapper tipoDatoMapper;

    @Autowired
    private TipoDatoService tipoDatoService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.TipoDatoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoDatoSearchRepository mockTipoDatoSearchRepository;

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

    private MockMvc restTipoDatoMockMvc;

    private TipoDato tipoDato;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoDatoResource tipoDatoResource = new TipoDatoResource(tipoDatoService);
        this.restTipoDatoMockMvc = MockMvcBuilders.standaloneSetup(tipoDatoResource)
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
    public static TipoDato createEntity(EntityManager em) {
        TipoDato tipoDato = new TipoDato()
            .nombre(DEFAULT_NOMBRE);
        return tipoDato;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoDato createUpdatedEntity(EntityManager em) {
        TipoDato tipoDato = new TipoDato()
            .nombre(UPDATED_NOMBRE);
        return tipoDato;
    }

    @BeforeEach
    public void initTest() {
        tipoDato = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoDato() throws Exception {
        int databaseSizeBeforeCreate = tipoDatoRepository.findAll().size();

        // Create the TipoDato
        TipoDatoDTO tipoDatoDTO = tipoDatoMapper.toDto(tipoDato);
        restTipoDatoMockMvc.perform(post("/api/tipo-datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDatoDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoDato in the database
        List<TipoDato> tipoDatoList = tipoDatoRepository.findAll();
        assertThat(tipoDatoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoDato testTipoDato = tipoDatoList.get(tipoDatoList.size() - 1);
        assertThat(testTipoDato.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the TipoDato in Elasticsearch
        verify(mockTipoDatoSearchRepository, times(1)).save(testTipoDato);
    }

    @Test
    @Transactional
    public void createTipoDatoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoDatoRepository.findAll().size();

        // Create the TipoDato with an existing ID
        tipoDato.setId(1L);
        TipoDatoDTO tipoDatoDTO = tipoDatoMapper.toDto(tipoDato);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoDatoMockMvc.perform(post("/api/tipo-datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDatoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDato in the database
        List<TipoDato> tipoDatoList = tipoDatoRepository.findAll();
        assertThat(tipoDatoList).hasSize(databaseSizeBeforeCreate);

        // Validate the TipoDato in Elasticsearch
        verify(mockTipoDatoSearchRepository, times(0)).save(tipoDato);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDatoRepository.findAll().size();
        // set the field null
        tipoDato.setNombre(null);

        // Create the TipoDato, which fails.
        TipoDatoDTO tipoDatoDTO = tipoDatoMapper.toDto(tipoDato);

        restTipoDatoMockMvc.perform(post("/api/tipo-datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDatoDTO)))
            .andExpect(status().isBadRequest());

        List<TipoDato> tipoDatoList = tipoDatoRepository.findAll();
        assertThat(tipoDatoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoDatoes() throws Exception {
        // Initialize the database
        tipoDatoRepository.saveAndFlush(tipoDato);

        // Get all the tipoDatoList
        restTipoDatoMockMvc.perform(get("/api/tipo-datoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDato.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getTipoDato() throws Exception {
        // Initialize the database
        tipoDatoRepository.saveAndFlush(tipoDato);

        // Get the tipoDato
        restTipoDatoMockMvc.perform(get("/api/tipo-datoes/{id}", tipoDato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoDato.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    public void getNonExistingTipoDato() throws Exception {
        // Get the tipoDato
        restTipoDatoMockMvc.perform(get("/api/tipo-datoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoDato() throws Exception {
        // Initialize the database
        tipoDatoRepository.saveAndFlush(tipoDato);

        int databaseSizeBeforeUpdate = tipoDatoRepository.findAll().size();

        // Update the tipoDato
        TipoDato updatedTipoDato = tipoDatoRepository.findById(tipoDato.getId()).get();
        // Disconnect from session so that the updates on updatedTipoDato are not directly saved in db
        em.detach(updatedTipoDato);
        updatedTipoDato
            .nombre(UPDATED_NOMBRE);
        TipoDatoDTO tipoDatoDTO = tipoDatoMapper.toDto(updatedTipoDato);

        restTipoDatoMockMvc.perform(put("/api/tipo-datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDatoDTO)))
            .andExpect(status().isOk());

        // Validate the TipoDato in the database
        List<TipoDato> tipoDatoList = tipoDatoRepository.findAll();
        assertThat(tipoDatoList).hasSize(databaseSizeBeforeUpdate);
        TipoDato testTipoDato = tipoDatoList.get(tipoDatoList.size() - 1);
        assertThat(testTipoDato.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the TipoDato in Elasticsearch
        verify(mockTipoDatoSearchRepository, times(1)).save(testTipoDato);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoDato() throws Exception {
        int databaseSizeBeforeUpdate = tipoDatoRepository.findAll().size();

        // Create the TipoDato
        TipoDatoDTO tipoDatoDTO = tipoDatoMapper.toDto(tipoDato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoDatoMockMvc.perform(put("/api/tipo-datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDatoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDato in the database
        List<TipoDato> tipoDatoList = tipoDatoRepository.findAll();
        assertThat(tipoDatoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoDato in Elasticsearch
        verify(mockTipoDatoSearchRepository, times(0)).save(tipoDato);
    }

    @Test
    @Transactional
    public void deleteTipoDato() throws Exception {
        // Initialize the database
        tipoDatoRepository.saveAndFlush(tipoDato);

        int databaseSizeBeforeDelete = tipoDatoRepository.findAll().size();

        // Delete the tipoDato
        restTipoDatoMockMvc.perform(delete("/api/tipo-datoes/{id}", tipoDato.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoDato> tipoDatoList = tipoDatoRepository.findAll();
        assertThat(tipoDatoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TipoDato in Elasticsearch
        verify(mockTipoDatoSearchRepository, times(1)).deleteById(tipoDato.getId());
    }

    @Test
    @Transactional
    public void searchTipoDato() throws Exception {
        // Initialize the database
        tipoDatoRepository.saveAndFlush(tipoDato);
        when(mockTipoDatoSearchRepository.search(queryStringQuery("id:" + tipoDato.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipoDato), PageRequest.of(0, 1), 1));
        // Search the tipoDato
        restTipoDatoMockMvc.perform(get("/api/_search/tipo-datoes?query=id:" + tipoDato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDato.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDato.class);
        TipoDato tipoDato1 = new TipoDato();
        tipoDato1.setId(1L);
        TipoDato tipoDato2 = new TipoDato();
        tipoDato2.setId(tipoDato1.getId());
        assertThat(tipoDato1).isEqualTo(tipoDato2);
        tipoDato2.setId(2L);
        assertThat(tipoDato1).isNotEqualTo(tipoDato2);
        tipoDato1.setId(null);
        assertThat(tipoDato1).isNotEqualTo(tipoDato2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDatoDTO.class);
        TipoDatoDTO tipoDatoDTO1 = new TipoDatoDTO();
        tipoDatoDTO1.setId(1L);
        TipoDatoDTO tipoDatoDTO2 = new TipoDatoDTO();
        assertThat(tipoDatoDTO1).isNotEqualTo(tipoDatoDTO2);
        tipoDatoDTO2.setId(tipoDatoDTO1.getId());
        assertThat(tipoDatoDTO1).isEqualTo(tipoDatoDTO2);
        tipoDatoDTO2.setId(2L);
        assertThat(tipoDatoDTO1).isNotEqualTo(tipoDatoDTO2);
        tipoDatoDTO1.setId(null);
        assertThat(tipoDatoDTO1).isNotEqualTo(tipoDatoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoDatoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoDatoMapper.fromId(null)).isNull();
    }
}
