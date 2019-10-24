package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.CronogramaEtapaActividad;
import scjn.gob.mx.domain.CronogramaEtapa;
import scjn.gob.mx.repository.CronogramaEtapaActividadRepository;
import scjn.gob.mx.repository.search.CronogramaEtapaActividadSearchRepository;
import scjn.gob.mx.service.CronogramaEtapaActividadService;
import scjn.gob.mx.service.dto.CronogramaEtapaActividadDTO;
import scjn.gob.mx.service.mapper.CronogramaEtapaActividadMapper;
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
 * Integration tests for the {@link CronogramaEtapaActividadResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class CronogramaEtapaActividadResourceIT {

    private static final Integer DEFAULT_ANIO = 1;
    private static final Integer UPDATED_ANIO = 2;

    private static final Integer DEFAULT_MES = 1;
    private static final Integer UPDATED_MES = 2;

    private static final Integer DEFAULT_SEMANA = 1;
    private static final Integer UPDATED_SEMANA = 2;

    private static final Boolean DEFAULT_REALIZADO = false;
    private static final Boolean UPDATED_REALIZADO = true;

    @Autowired
    private CronogramaEtapaActividadRepository cronogramaEtapaActividadRepository;

    @Autowired
    private CronogramaEtapaActividadMapper cronogramaEtapaActividadMapper;

    @Autowired
    private CronogramaEtapaActividadService cronogramaEtapaActividadService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.CronogramaEtapaActividadSearchRepositoryMockConfiguration
     */
    @Autowired
    private CronogramaEtapaActividadSearchRepository mockCronogramaEtapaActividadSearchRepository;

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

    private MockMvc restCronogramaEtapaActividadMockMvc;

    private CronogramaEtapaActividad cronogramaEtapaActividad;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CronogramaEtapaActividadResource cronogramaEtapaActividadResource = new CronogramaEtapaActividadResource(cronogramaEtapaActividadService);
        this.restCronogramaEtapaActividadMockMvc = MockMvcBuilders.standaloneSetup(cronogramaEtapaActividadResource)
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
    public static CronogramaEtapaActividad createEntity(EntityManager em) {
        CronogramaEtapaActividad cronogramaEtapaActividad = new CronogramaEtapaActividad()
            .anio(DEFAULT_ANIO)
            .mes(DEFAULT_MES)
            .semana(DEFAULT_SEMANA)
            .realizado(DEFAULT_REALIZADO);
        // Add required entity
        CronogramaEtapa cronogramaEtapa;
        if (TestUtil.findAll(em, CronogramaEtapa.class).isEmpty()) {
            cronogramaEtapa = CronogramaEtapaResourceIT.createEntity(em);
            em.persist(cronogramaEtapa);
            em.flush();
        } else {
            cronogramaEtapa = TestUtil.findAll(em, CronogramaEtapa.class).get(0);
        }
        cronogramaEtapaActividad.setCronogramaEtapa(cronogramaEtapa);
        return cronogramaEtapaActividad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronogramaEtapaActividad createUpdatedEntity(EntityManager em) {
        CronogramaEtapaActividad cronogramaEtapaActividad = new CronogramaEtapaActividad()
            .anio(UPDATED_ANIO)
            .mes(UPDATED_MES)
            .semana(UPDATED_SEMANA)
            .realizado(UPDATED_REALIZADO);
        // Add required entity
        CronogramaEtapa cronogramaEtapa;
        if (TestUtil.findAll(em, CronogramaEtapa.class).isEmpty()) {
            cronogramaEtapa = CronogramaEtapaResourceIT.createUpdatedEntity(em);
            em.persist(cronogramaEtapa);
            em.flush();
        } else {
            cronogramaEtapa = TestUtil.findAll(em, CronogramaEtapa.class).get(0);
        }
        cronogramaEtapaActividad.setCronogramaEtapa(cronogramaEtapa);
        return cronogramaEtapaActividad;
    }

    @BeforeEach
    public void initTest() {
        cronogramaEtapaActividad = createEntity(em);
    }

    @Test
    @Transactional
    public void createCronogramaEtapaActividad() throws Exception {
        int databaseSizeBeforeCreate = cronogramaEtapaActividadRepository.findAll().size();

        // Create the CronogramaEtapaActividad
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);
        restCronogramaEtapaActividadMockMvc.perform(post("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isCreated());

        // Validate the CronogramaEtapaActividad in the database
        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeCreate + 1);
        CronogramaEtapaActividad testCronogramaEtapaActividad = cronogramaEtapaActividadList.get(cronogramaEtapaActividadList.size() - 1);
        assertThat(testCronogramaEtapaActividad.getAnio()).isEqualTo(DEFAULT_ANIO);
        assertThat(testCronogramaEtapaActividad.getMes()).isEqualTo(DEFAULT_MES);
        assertThat(testCronogramaEtapaActividad.getSemana()).isEqualTo(DEFAULT_SEMANA);
        assertThat(testCronogramaEtapaActividad.isRealizado()).isEqualTo(DEFAULT_REALIZADO);

        // Validate the CronogramaEtapaActividad in Elasticsearch
        verify(mockCronogramaEtapaActividadSearchRepository, times(1)).save(testCronogramaEtapaActividad);
    }

    @Test
    @Transactional
    public void createCronogramaEtapaActividadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cronogramaEtapaActividadRepository.findAll().size();

        // Create the CronogramaEtapaActividad with an existing ID
        cronogramaEtapaActividad.setId(1L);
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronogramaEtapaActividadMockMvc.perform(post("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CronogramaEtapaActividad in the database
        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeCreate);

        // Validate the CronogramaEtapaActividad in Elasticsearch
        verify(mockCronogramaEtapaActividadSearchRepository, times(0)).save(cronogramaEtapaActividad);
    }


    @Test
    @Transactional
    public void checkAnioIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronogramaEtapaActividadRepository.findAll().size();
        // set the field null
        cronogramaEtapaActividad.setAnio(null);

        // Create the CronogramaEtapaActividad, which fails.
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);

        restCronogramaEtapaActividadMockMvc.perform(post("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isBadRequest());

        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMesIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronogramaEtapaActividadRepository.findAll().size();
        // set the field null
        cronogramaEtapaActividad.setMes(null);

        // Create the CronogramaEtapaActividad, which fails.
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);

        restCronogramaEtapaActividadMockMvc.perform(post("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isBadRequest());

        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSemanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronogramaEtapaActividadRepository.findAll().size();
        // set the field null
        cronogramaEtapaActividad.setSemana(null);

        // Create the CronogramaEtapaActividad, which fails.
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);

        restCronogramaEtapaActividadMockMvc.perform(post("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isBadRequest());

        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCronogramaEtapaActividads() throws Exception {
        // Initialize the database
        cronogramaEtapaActividadRepository.saveAndFlush(cronogramaEtapaActividad);

        // Get all the cronogramaEtapaActividadList
        restCronogramaEtapaActividadMockMvc.perform(get("/api/cronograma-etapa-actividads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronogramaEtapaActividad.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES)))
            .andExpect(jsonPath("$.[*].semana").value(hasItem(DEFAULT_SEMANA)))
            .andExpect(jsonPath("$.[*].realizado").value(hasItem(DEFAULT_REALIZADO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCronogramaEtapaActividad() throws Exception {
        // Initialize the database
        cronogramaEtapaActividadRepository.saveAndFlush(cronogramaEtapaActividad);

        // Get the cronogramaEtapaActividad
        restCronogramaEtapaActividadMockMvc.perform(get("/api/cronograma-etapa-actividads/{id}", cronogramaEtapaActividad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cronogramaEtapaActividad.getId().intValue()))
            .andExpect(jsonPath("$.anio").value(DEFAULT_ANIO))
            .andExpect(jsonPath("$.mes").value(DEFAULT_MES))
            .andExpect(jsonPath("$.semana").value(DEFAULT_SEMANA))
            .andExpect(jsonPath("$.realizado").value(DEFAULT_REALIZADO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCronogramaEtapaActividad() throws Exception {
        // Get the cronogramaEtapaActividad
        restCronogramaEtapaActividadMockMvc.perform(get("/api/cronograma-etapa-actividads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCronogramaEtapaActividad() throws Exception {
        // Initialize the database
        cronogramaEtapaActividadRepository.saveAndFlush(cronogramaEtapaActividad);

        int databaseSizeBeforeUpdate = cronogramaEtapaActividadRepository.findAll().size();

        // Update the cronogramaEtapaActividad
        CronogramaEtapaActividad updatedCronogramaEtapaActividad = cronogramaEtapaActividadRepository.findById(cronogramaEtapaActividad.getId()).get();
        // Disconnect from session so that the updates on updatedCronogramaEtapaActividad are not directly saved in db
        em.detach(updatedCronogramaEtapaActividad);
        updatedCronogramaEtapaActividad
            .anio(UPDATED_ANIO)
            .mes(UPDATED_MES)
            .semana(UPDATED_SEMANA)
            .realizado(UPDATED_REALIZADO);
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(updatedCronogramaEtapaActividad);

        restCronogramaEtapaActividadMockMvc.perform(put("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isOk());

        // Validate the CronogramaEtapaActividad in the database
        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeUpdate);
        CronogramaEtapaActividad testCronogramaEtapaActividad = cronogramaEtapaActividadList.get(cronogramaEtapaActividadList.size() - 1);
        assertThat(testCronogramaEtapaActividad.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testCronogramaEtapaActividad.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testCronogramaEtapaActividad.getSemana()).isEqualTo(UPDATED_SEMANA);
        assertThat(testCronogramaEtapaActividad.isRealizado()).isEqualTo(UPDATED_REALIZADO);

        // Validate the CronogramaEtapaActividad in Elasticsearch
        verify(mockCronogramaEtapaActividadSearchRepository, times(1)).save(testCronogramaEtapaActividad);
    }

    @Test
    @Transactional
    public void updateNonExistingCronogramaEtapaActividad() throws Exception {
        int databaseSizeBeforeUpdate = cronogramaEtapaActividadRepository.findAll().size();

        // Create the CronogramaEtapaActividad
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO = cronogramaEtapaActividadMapper.toDto(cronogramaEtapaActividad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronogramaEtapaActividadMockMvc.perform(put("/api/cronograma-etapa-actividads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaEtapaActividadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CronogramaEtapaActividad in the database
        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CronogramaEtapaActividad in Elasticsearch
        verify(mockCronogramaEtapaActividadSearchRepository, times(0)).save(cronogramaEtapaActividad);
    }

    @Test
    @Transactional
    public void deleteCronogramaEtapaActividad() throws Exception {
        // Initialize the database
        cronogramaEtapaActividadRepository.saveAndFlush(cronogramaEtapaActividad);

        int databaseSizeBeforeDelete = cronogramaEtapaActividadRepository.findAll().size();

        // Delete the cronogramaEtapaActividad
        restCronogramaEtapaActividadMockMvc.perform(delete("/api/cronograma-etapa-actividads/{id}", cronogramaEtapaActividad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CronogramaEtapaActividad> cronogramaEtapaActividadList = cronogramaEtapaActividadRepository.findAll();
        assertThat(cronogramaEtapaActividadList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CronogramaEtapaActividad in Elasticsearch
        verify(mockCronogramaEtapaActividadSearchRepository, times(1)).deleteById(cronogramaEtapaActividad.getId());
    }

    @Test
    @Transactional
    public void searchCronogramaEtapaActividad() throws Exception {
        // Initialize the database
        cronogramaEtapaActividadRepository.saveAndFlush(cronogramaEtapaActividad);
        when(mockCronogramaEtapaActividadSearchRepository.search(queryStringQuery("id:" + cronogramaEtapaActividad.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cronogramaEtapaActividad), PageRequest.of(0, 1), 1));
        // Search the cronogramaEtapaActividad
        restCronogramaEtapaActividadMockMvc.perform(get("/api/_search/cronograma-etapa-actividads?query=id:" + cronogramaEtapaActividad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronogramaEtapaActividad.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES)))
            .andExpect(jsonPath("$.[*].semana").value(hasItem(DEFAULT_SEMANA)))
            .andExpect(jsonPath("$.[*].realizado").value(hasItem(DEFAULT_REALIZADO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronogramaEtapaActividad.class);
        CronogramaEtapaActividad cronogramaEtapaActividad1 = new CronogramaEtapaActividad();
        cronogramaEtapaActividad1.setId(1L);
        CronogramaEtapaActividad cronogramaEtapaActividad2 = new CronogramaEtapaActividad();
        cronogramaEtapaActividad2.setId(cronogramaEtapaActividad1.getId());
        assertThat(cronogramaEtapaActividad1).isEqualTo(cronogramaEtapaActividad2);
        cronogramaEtapaActividad2.setId(2L);
        assertThat(cronogramaEtapaActividad1).isNotEqualTo(cronogramaEtapaActividad2);
        cronogramaEtapaActividad1.setId(null);
        assertThat(cronogramaEtapaActividad1).isNotEqualTo(cronogramaEtapaActividad2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronogramaEtapaActividadDTO.class);
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO1 = new CronogramaEtapaActividadDTO();
        cronogramaEtapaActividadDTO1.setId(1L);
        CronogramaEtapaActividadDTO cronogramaEtapaActividadDTO2 = new CronogramaEtapaActividadDTO();
        assertThat(cronogramaEtapaActividadDTO1).isNotEqualTo(cronogramaEtapaActividadDTO2);
        cronogramaEtapaActividadDTO2.setId(cronogramaEtapaActividadDTO1.getId());
        assertThat(cronogramaEtapaActividadDTO1).isEqualTo(cronogramaEtapaActividadDTO2);
        cronogramaEtapaActividadDTO2.setId(2L);
        assertThat(cronogramaEtapaActividadDTO1).isNotEqualTo(cronogramaEtapaActividadDTO2);
        cronogramaEtapaActividadDTO1.setId(null);
        assertThat(cronogramaEtapaActividadDTO1).isNotEqualTo(cronogramaEtapaActividadDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cronogramaEtapaActividadMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cronogramaEtapaActividadMapper.fromId(null)).isNull();
    }
}
