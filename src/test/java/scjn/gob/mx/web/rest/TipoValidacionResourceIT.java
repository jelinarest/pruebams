package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.TipoValidacion;
import scjn.gob.mx.repository.TipoValidacionRepository;
import scjn.gob.mx.repository.search.TipoValidacionSearchRepository;
import scjn.gob.mx.service.TipoValidacionService;
import scjn.gob.mx.service.dto.TipoValidacionDTO;
import scjn.gob.mx.service.mapper.TipoValidacionMapper;
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
 * Integration tests for the {@link TipoValidacionResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class TipoValidacionResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPRESION_REGULAR = "AAAAAAAAAA";
    private static final String UPDATED_EXPRESION_REGULAR = "BBBBBBBBBB";

    private static final String DEFAULT_TEXTO_AYUDA = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO_AYUDA = "BBBBBBBBBB";

    @Autowired
    private TipoValidacionRepository tipoValidacionRepository;

    @Autowired
    private TipoValidacionMapper tipoValidacionMapper;

    @Autowired
    private TipoValidacionService tipoValidacionService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.TipoValidacionSearchRepositoryMockConfiguration
     */
    @Autowired
    private TipoValidacionSearchRepository mockTipoValidacionSearchRepository;

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

    private MockMvc restTipoValidacionMockMvc;

    private TipoValidacion tipoValidacion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoValidacionResource tipoValidacionResource = new TipoValidacionResource(tipoValidacionService);
        this.restTipoValidacionMockMvc = MockMvcBuilders.standaloneSetup(tipoValidacionResource)
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
    public static TipoValidacion createEntity(EntityManager em) {
        TipoValidacion tipoValidacion = new TipoValidacion()
            .nombre(DEFAULT_NOMBRE)
            .expresionRegular(DEFAULT_EXPRESION_REGULAR)
            .textoAyuda(DEFAULT_TEXTO_AYUDA);
        return tipoValidacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoValidacion createUpdatedEntity(EntityManager em) {
        TipoValidacion tipoValidacion = new TipoValidacion()
            .nombre(UPDATED_NOMBRE)
            .expresionRegular(UPDATED_EXPRESION_REGULAR)
            .textoAyuda(UPDATED_TEXTO_AYUDA);
        return tipoValidacion;
    }

    @BeforeEach
    public void initTest() {
        tipoValidacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoValidacion() throws Exception {
        int databaseSizeBeforeCreate = tipoValidacionRepository.findAll().size();

        // Create the TipoValidacion
        TipoValidacionDTO tipoValidacionDTO = tipoValidacionMapper.toDto(tipoValidacion);
        restTipoValidacionMockMvc.perform(post("/api/tipo-validacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoValidacionDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoValidacion in the database
        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeCreate + 1);
        TipoValidacion testTipoValidacion = tipoValidacionList.get(tipoValidacionList.size() - 1);
        assertThat(testTipoValidacion.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoValidacion.getExpresionRegular()).isEqualTo(DEFAULT_EXPRESION_REGULAR);
        assertThat(testTipoValidacion.getTextoAyuda()).isEqualTo(DEFAULT_TEXTO_AYUDA);

        // Validate the TipoValidacion in Elasticsearch
        verify(mockTipoValidacionSearchRepository, times(1)).save(testTipoValidacion);
    }

    @Test
    @Transactional
    public void createTipoValidacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoValidacionRepository.findAll().size();

        // Create the TipoValidacion with an existing ID
        tipoValidacion.setId(1L);
        TipoValidacionDTO tipoValidacionDTO = tipoValidacionMapper.toDto(tipoValidacion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoValidacionMockMvc.perform(post("/api/tipo-validacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoValidacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoValidacion in the database
        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeCreate);

        // Validate the TipoValidacion in Elasticsearch
        verify(mockTipoValidacionSearchRepository, times(0)).save(tipoValidacion);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoValidacionRepository.findAll().size();
        // set the field null
        tipoValidacion.setNombre(null);

        // Create the TipoValidacion, which fails.
        TipoValidacionDTO tipoValidacionDTO = tipoValidacionMapper.toDto(tipoValidacion);

        restTipoValidacionMockMvc.perform(post("/api/tipo-validacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoValidacionDTO)))
            .andExpect(status().isBadRequest());

        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpresionRegularIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoValidacionRepository.findAll().size();
        // set the field null
        tipoValidacion.setExpresionRegular(null);

        // Create the TipoValidacion, which fails.
        TipoValidacionDTO tipoValidacionDTO = tipoValidacionMapper.toDto(tipoValidacion);

        restTipoValidacionMockMvc.perform(post("/api/tipo-validacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoValidacionDTO)))
            .andExpect(status().isBadRequest());

        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoValidacions() throws Exception {
        // Initialize the database
        tipoValidacionRepository.saveAndFlush(tipoValidacion);

        // Get all the tipoValidacionList
        restTipoValidacionMockMvc.perform(get("/api/tipo-validacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoValidacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].expresionRegular").value(hasItem(DEFAULT_EXPRESION_REGULAR)))
            .andExpect(jsonPath("$.[*].textoAyuda").value(hasItem(DEFAULT_TEXTO_AYUDA)));
    }
    
    @Test
    @Transactional
    public void getTipoValidacion() throws Exception {
        // Initialize the database
        tipoValidacionRepository.saveAndFlush(tipoValidacion);

        // Get the tipoValidacion
        restTipoValidacionMockMvc.perform(get("/api/tipo-validacions/{id}", tipoValidacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoValidacion.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.expresionRegular").value(DEFAULT_EXPRESION_REGULAR))
            .andExpect(jsonPath("$.textoAyuda").value(DEFAULT_TEXTO_AYUDA));
    }

    @Test
    @Transactional
    public void getNonExistingTipoValidacion() throws Exception {
        // Get the tipoValidacion
        restTipoValidacionMockMvc.perform(get("/api/tipo-validacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoValidacion() throws Exception {
        // Initialize the database
        tipoValidacionRepository.saveAndFlush(tipoValidacion);

        int databaseSizeBeforeUpdate = tipoValidacionRepository.findAll().size();

        // Update the tipoValidacion
        TipoValidacion updatedTipoValidacion = tipoValidacionRepository.findById(tipoValidacion.getId()).get();
        // Disconnect from session so that the updates on updatedTipoValidacion are not directly saved in db
        em.detach(updatedTipoValidacion);
        updatedTipoValidacion
            .nombre(UPDATED_NOMBRE)
            .expresionRegular(UPDATED_EXPRESION_REGULAR)
            .textoAyuda(UPDATED_TEXTO_AYUDA);
        TipoValidacionDTO tipoValidacionDTO = tipoValidacionMapper.toDto(updatedTipoValidacion);

        restTipoValidacionMockMvc.perform(put("/api/tipo-validacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoValidacionDTO)))
            .andExpect(status().isOk());

        // Validate the TipoValidacion in the database
        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeUpdate);
        TipoValidacion testTipoValidacion = tipoValidacionList.get(tipoValidacionList.size() - 1);
        assertThat(testTipoValidacion.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoValidacion.getExpresionRegular()).isEqualTo(UPDATED_EXPRESION_REGULAR);
        assertThat(testTipoValidacion.getTextoAyuda()).isEqualTo(UPDATED_TEXTO_AYUDA);

        // Validate the TipoValidacion in Elasticsearch
        verify(mockTipoValidacionSearchRepository, times(1)).save(testTipoValidacion);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoValidacion() throws Exception {
        int databaseSizeBeforeUpdate = tipoValidacionRepository.findAll().size();

        // Create the TipoValidacion
        TipoValidacionDTO tipoValidacionDTO = tipoValidacionMapper.toDto(tipoValidacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoValidacionMockMvc.perform(put("/api/tipo-validacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoValidacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoValidacion in the database
        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TipoValidacion in Elasticsearch
        verify(mockTipoValidacionSearchRepository, times(0)).save(tipoValidacion);
    }

    @Test
    @Transactional
    public void deleteTipoValidacion() throws Exception {
        // Initialize the database
        tipoValidacionRepository.saveAndFlush(tipoValidacion);

        int databaseSizeBeforeDelete = tipoValidacionRepository.findAll().size();

        // Delete the tipoValidacion
        restTipoValidacionMockMvc.perform(delete("/api/tipo-validacions/{id}", tipoValidacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoValidacion> tipoValidacionList = tipoValidacionRepository.findAll();
        assertThat(tipoValidacionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TipoValidacion in Elasticsearch
        verify(mockTipoValidacionSearchRepository, times(1)).deleteById(tipoValidacion.getId());
    }

    @Test
    @Transactional
    public void searchTipoValidacion() throws Exception {
        // Initialize the database
        tipoValidacionRepository.saveAndFlush(tipoValidacion);
        when(mockTipoValidacionSearchRepository.search(queryStringQuery("id:" + tipoValidacion.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tipoValidacion), PageRequest.of(0, 1), 1));
        // Search the tipoValidacion
        restTipoValidacionMockMvc.perform(get("/api/_search/tipo-validacions?query=id:" + tipoValidacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoValidacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].expresionRegular").value(hasItem(DEFAULT_EXPRESION_REGULAR)))
            .andExpect(jsonPath("$.[*].textoAyuda").value(hasItem(DEFAULT_TEXTO_AYUDA)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoValidacion.class);
        TipoValidacion tipoValidacion1 = new TipoValidacion();
        tipoValidacion1.setId(1L);
        TipoValidacion tipoValidacion2 = new TipoValidacion();
        tipoValidacion2.setId(tipoValidacion1.getId());
        assertThat(tipoValidacion1).isEqualTo(tipoValidacion2);
        tipoValidacion2.setId(2L);
        assertThat(tipoValidacion1).isNotEqualTo(tipoValidacion2);
        tipoValidacion1.setId(null);
        assertThat(tipoValidacion1).isNotEqualTo(tipoValidacion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoValidacionDTO.class);
        TipoValidacionDTO tipoValidacionDTO1 = new TipoValidacionDTO();
        tipoValidacionDTO1.setId(1L);
        TipoValidacionDTO tipoValidacionDTO2 = new TipoValidacionDTO();
        assertThat(tipoValidacionDTO1).isNotEqualTo(tipoValidacionDTO2);
        tipoValidacionDTO2.setId(tipoValidacionDTO1.getId());
        assertThat(tipoValidacionDTO1).isEqualTo(tipoValidacionDTO2);
        tipoValidacionDTO2.setId(2L);
        assertThat(tipoValidacionDTO1).isNotEqualTo(tipoValidacionDTO2);
        tipoValidacionDTO1.setId(null);
        assertThat(tipoValidacionDTO1).isNotEqualTo(tipoValidacionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tipoValidacionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tipoValidacionMapper.fromId(null)).isNull();
    }
}
