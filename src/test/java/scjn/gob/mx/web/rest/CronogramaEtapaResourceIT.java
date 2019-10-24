package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.CronogramaEtapa;
import scjn.gob.mx.domain.Cronograma;
import scjn.gob.mx.repository.CronogramaEtapaRepository;
import scjn.gob.mx.repository.search.CronogramaEtapaSearchRepository;
import scjn.gob.mx.service.CronogramaEtapaService;
import scjn.gob.mx.service.dto.CronogramaEtapaDTO;
import scjn.gob.mx.service.mapper.CronogramaEtapaMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static scjn.gob.mx.web.rest.TestUtil.sameInstant;
import static scjn.gob.mx.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CronogramaEtapaResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class CronogramaEtapaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_ALTA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_ALTA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;

    @Autowired
    private CronogramaEtapaRepository cronogramaEtapaRepository;

    @Autowired
    private CronogramaEtapaMapper cronogramaEtapaMapper;

    @Autowired
    private CronogramaEtapaService cronogramaEtapaService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.CronogramaEtapaSearchRepositoryMockConfiguration
     */
    @Autowired
    private CronogramaEtapaSearchRepository mockCronogramaEtapaSearchRepository;

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

    private MockMvc restCronogramaEtapaMockMvc;

    private CronogramaEtapa cronogramaEtapa;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CronogramaEtapaResource cronogramaEtapaResource = new CronogramaEtapaResource(cronogramaEtapaService);
        this.restCronogramaEtapaMockMvc = MockMvcBuilders.standaloneSetup(cronogramaEtapaResource)
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
    public static CronogramaEtapa createEntity(EntityManager em) {
        CronogramaEtapa cronogramaEtapa = new CronogramaEtapa()
            .nombre(DEFAULT_NOMBRE)
            .notas(DEFAULT_NOTAS)
            .fechaAlta(DEFAULT_FECHA_ALTA)
            .activo(DEFAULT_ACTIVO)
            .orden(DEFAULT_ORDEN);
        // Add required entity
        Cronograma cronograma;
        if (TestUtil.findAll(em, Cronograma.class).isEmpty()) {
            cronograma = CronogramaResourceIT.createEntity(em);
            em.persist(cronograma);
            em.flush();
        } else {
            cronograma = TestUtil.findAll(em, Cronograma.class).get(0);
        }
        cronogramaEtapa.setCronograma(cronograma);
        return cronogramaEtapa;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronogramaEtapa createUpdatedEntity(EntityManager em) {
        CronogramaEtapa cronogramaEtapa = new CronogramaEtapa()
            .nombre(UPDATED_NOMBRE)
            .notas(UPDATED_NOTAS)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .activo(UPDATED_ACTIVO)
            .orden(UPDATED_ORDEN);
        // Add required entity
        Cronograma cronograma;
        if (TestUtil.findAll(em, Cronograma.class).isEmpty()) {
            cronograma = CronogramaResourceIT.createUpdatedEntity(em);
            em.persist(cronograma);
            em.flush();
        } else {
            cronograma = TestUtil.findAll(em, Cronograma.class).get(0);
        }
        cronogramaEtapa.setCronograma(cronograma);
        return cronogramaEtapa;
    }

    @BeforeEach
    public void initTest() {
        cronogramaEtapa = createEntity(em);
    }

    @Test
    @Transactional
    public void createCronogramaEtapa() throws Exception {
        int databaseSizeBeforeCreate = cronogramaEtapaRepository.findAll().size();

        // Create the CronogramaEtapa
        CronogramaEtapaDTO cronogramaEtapaDTO = cronogramaEtapaMapper.toDto(cronogramaEtapa);
        restCronogramaEtapaMockMvc.perform(post("/api/cronograma-etapas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaDTO)))
            .andExpect(status().isCreated());

        // Validate the CronogramaEtapa in the database
        List<CronogramaEtapa> cronogramaEtapaList = cronogramaEtapaRepository.findAll();
        assertThat(cronogramaEtapaList).hasSize(databaseSizeBeforeCreate + 1);
        CronogramaEtapa testCronogramaEtapa = cronogramaEtapaList.get(cronogramaEtapaList.size() - 1);
        assertThat(testCronogramaEtapa.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCronogramaEtapa.getNotas()).isEqualTo(DEFAULT_NOTAS);
        assertThat(testCronogramaEtapa.getFechaAlta()).isEqualTo(DEFAULT_FECHA_ALTA);
        assertThat(testCronogramaEtapa.isActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testCronogramaEtapa.getOrden()).isEqualTo(DEFAULT_ORDEN);

        // Validate the CronogramaEtapa in Elasticsearch
        verify(mockCronogramaEtapaSearchRepository, times(1)).save(testCronogramaEtapa);
    }

    @Test
    @Transactional
    public void createCronogramaEtapaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cronogramaEtapaRepository.findAll().size();

        // Create the CronogramaEtapa with an existing ID
        cronogramaEtapa.setId(1L);
        CronogramaEtapaDTO cronogramaEtapaDTO = cronogramaEtapaMapper.toDto(cronogramaEtapa);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronogramaEtapaMockMvc.perform(post("/api/cronograma-etapas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CronogramaEtapa in the database
        List<CronogramaEtapa> cronogramaEtapaList = cronogramaEtapaRepository.findAll();
        assertThat(cronogramaEtapaList).hasSize(databaseSizeBeforeCreate);

        // Validate the CronogramaEtapa in Elasticsearch
        verify(mockCronogramaEtapaSearchRepository, times(0)).save(cronogramaEtapa);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronogramaEtapaRepository.findAll().size();
        // set the field null
        cronogramaEtapa.setNombre(null);

        // Create the CronogramaEtapa, which fails.
        CronogramaEtapaDTO cronogramaEtapaDTO = cronogramaEtapaMapper.toDto(cronogramaEtapa);

        restCronogramaEtapaMockMvc.perform(post("/api/cronograma-etapas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaDTO)))
            .andExpect(status().isBadRequest());

        List<CronogramaEtapa> cronogramaEtapaList = cronogramaEtapaRepository.findAll();
        assertThat(cronogramaEtapaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCronogramaEtapas() throws Exception {
        // Initialize the database
        cronogramaEtapaRepository.saveAndFlush(cronogramaEtapa);

        // Get all the cronogramaEtapaList
        restCronogramaEtapaMockMvc.perform(get("/api/cronograma-etapas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronogramaEtapa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(sameInstant(DEFAULT_FECHA_ALTA))))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }
    
    @Test
    @Transactional
    public void getCronogramaEtapa() throws Exception {
        // Initialize the database
        cronogramaEtapaRepository.saveAndFlush(cronogramaEtapa);

        // Get the cronogramaEtapa
        restCronogramaEtapaMockMvc.perform(get("/api/cronograma-etapas/{id}", cronogramaEtapa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cronogramaEtapa.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS))
            .andExpect(jsonPath("$.fechaAlta").value(sameInstant(DEFAULT_FECHA_ALTA)))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN));
    }

    @Test
    @Transactional
    public void getNonExistingCronogramaEtapa() throws Exception {
        // Get the cronogramaEtapa
        restCronogramaEtapaMockMvc.perform(get("/api/cronograma-etapas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCronogramaEtapa() throws Exception {
        // Initialize the database
        cronogramaEtapaRepository.saveAndFlush(cronogramaEtapa);

        int databaseSizeBeforeUpdate = cronogramaEtapaRepository.findAll().size();

        // Update the cronogramaEtapa
        CronogramaEtapa updatedCronogramaEtapa = cronogramaEtapaRepository.findById(cronogramaEtapa.getId()).get();
        // Disconnect from session so that the updates on updatedCronogramaEtapa are not directly saved in db
        em.detach(updatedCronogramaEtapa);
        updatedCronogramaEtapa
            .nombre(UPDATED_NOMBRE)
            .notas(UPDATED_NOTAS)
            .fechaAlta(UPDATED_FECHA_ALTA)
            .activo(UPDATED_ACTIVO)
            .orden(UPDATED_ORDEN);
        CronogramaEtapaDTO cronogramaEtapaDTO = cronogramaEtapaMapper.toDto(updatedCronogramaEtapa);

        restCronogramaEtapaMockMvc.perform(put("/api/cronograma-etapas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaDTO)))
            .andExpect(status().isOk());

        // Validate the CronogramaEtapa in the database
        List<CronogramaEtapa> cronogramaEtapaList = cronogramaEtapaRepository.findAll();
        assertThat(cronogramaEtapaList).hasSize(databaseSizeBeforeUpdate);
        CronogramaEtapa testCronogramaEtapa = cronogramaEtapaList.get(cronogramaEtapaList.size() - 1);
        assertThat(testCronogramaEtapa.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCronogramaEtapa.getNotas()).isEqualTo(UPDATED_NOTAS);
        assertThat(testCronogramaEtapa.getFechaAlta()).isEqualTo(UPDATED_FECHA_ALTA);
        assertThat(testCronogramaEtapa.isActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testCronogramaEtapa.getOrden()).isEqualTo(UPDATED_ORDEN);

        // Validate the CronogramaEtapa in Elasticsearch
        verify(mockCronogramaEtapaSearchRepository, times(1)).save(testCronogramaEtapa);
    }

    @Test
    @Transactional
    public void updateNonExistingCronogramaEtapa() throws Exception {
        int databaseSizeBeforeUpdate = cronogramaEtapaRepository.findAll().size();

        // Create the CronogramaEtapa
        CronogramaEtapaDTO cronogramaEtapaDTO = cronogramaEtapaMapper.toDto(cronogramaEtapa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronogramaEtapaMockMvc.perform(put("/api/cronograma-etapas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CronogramaEtapa in the database
        List<CronogramaEtapa> cronogramaEtapaList = cronogramaEtapaRepository.findAll();
        assertThat(cronogramaEtapaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CronogramaEtapa in Elasticsearch
        verify(mockCronogramaEtapaSearchRepository, times(0)).save(cronogramaEtapa);
    }

    @Test
    @Transactional
    public void deleteCronogramaEtapa() throws Exception {
        // Initialize the database
        cronogramaEtapaRepository.saveAndFlush(cronogramaEtapa);

        int databaseSizeBeforeDelete = cronogramaEtapaRepository.findAll().size();

        // Delete the cronogramaEtapa
        restCronogramaEtapaMockMvc.perform(delete("/api/cronograma-etapas/{id}", cronogramaEtapa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CronogramaEtapa> cronogramaEtapaList = cronogramaEtapaRepository.findAll();
        assertThat(cronogramaEtapaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CronogramaEtapa in Elasticsearch
        verify(mockCronogramaEtapaSearchRepository, times(1)).deleteById(cronogramaEtapa.getId());
    }

    @Test
    @Transactional
    public void searchCronogramaEtapa() throws Exception {
        // Initialize the database
        cronogramaEtapaRepository.saveAndFlush(cronogramaEtapa);
        when(mockCronogramaEtapaSearchRepository.search(queryStringQuery("id:" + cronogramaEtapa.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cronogramaEtapa), PageRequest.of(0, 1), 1));
        // Search the cronogramaEtapa
        restCronogramaEtapaMockMvc.perform(get("/api/_search/cronograma-etapas?query=id:" + cronogramaEtapa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronogramaEtapa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(sameInstant(DEFAULT_FECHA_ALTA))))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronogramaEtapa.class);
        CronogramaEtapa cronogramaEtapa1 = new CronogramaEtapa();
        cronogramaEtapa1.setId(1L);
        CronogramaEtapa cronogramaEtapa2 = new CronogramaEtapa();
        cronogramaEtapa2.setId(cronogramaEtapa1.getId());
        assertThat(cronogramaEtapa1).isEqualTo(cronogramaEtapa2);
        cronogramaEtapa2.setId(2L);
        assertThat(cronogramaEtapa1).isNotEqualTo(cronogramaEtapa2);
        cronogramaEtapa1.setId(null);
        assertThat(cronogramaEtapa1).isNotEqualTo(cronogramaEtapa2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronogramaEtapaDTO.class);
        CronogramaEtapaDTO cronogramaEtapaDTO1 = new CronogramaEtapaDTO();
        cronogramaEtapaDTO1.setId(1L);
        CronogramaEtapaDTO cronogramaEtapaDTO2 = new CronogramaEtapaDTO();
        assertThat(cronogramaEtapaDTO1).isNotEqualTo(cronogramaEtapaDTO2);
        cronogramaEtapaDTO2.setId(cronogramaEtapaDTO1.getId());
        assertThat(cronogramaEtapaDTO1).isEqualTo(cronogramaEtapaDTO2);
        cronogramaEtapaDTO2.setId(2L);
        assertThat(cronogramaEtapaDTO1).isNotEqualTo(cronogramaEtapaDTO2);
        cronogramaEtapaDTO1.setId(null);
        assertThat(cronogramaEtapaDTO1).isNotEqualTo(cronogramaEtapaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cronogramaEtapaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cronogramaEtapaMapper.fromId(null)).isNull();
    }
}
