package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.Cronograma;
import scjn.gob.mx.repository.CronogramaRepository;
import scjn.gob.mx.repository.search.CronogramaSearchRepository;
import scjn.gob.mx.service.CronogramaService;
import scjn.gob.mx.service.dto.CronogramaDTO;
import scjn.gob.mx.service.mapper.CronogramaMapper;
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
 * Integration tests for the {@link CronogramaResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class CronogramaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO_AJUSTE = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_AJUSTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final ZonedDateTime DEFAULT_FECHA_ALTA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_ALTA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CronogramaRepository cronogramaRepository;

    @Autowired
    private CronogramaMapper cronogramaMapper;

    @Autowired
    private CronogramaService cronogramaService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.CronogramaSearchRepositoryMockConfiguration
     */
    @Autowired
    private CronogramaSearchRepository mockCronogramaSearchRepository;

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

    private MockMvc restCronogramaMockMvc;

    private Cronograma cronograma;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CronogramaResource cronogramaResource = new CronogramaResource(cronogramaService);
        this.restCronogramaMockMvc = MockMvcBuilders.standaloneSetup(cronogramaResource)
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
    public static Cronograma createEntity(EntityManager em) {
        Cronograma cronograma = new Cronograma()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .motivoAjuste(DEFAULT_MOTIVO_AJUSTE)
            .activo(DEFAULT_ACTIVO)
            .fechaAlta(DEFAULT_FECHA_ALTA);
        return cronograma;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cronograma createUpdatedEntity(EntityManager em) {
        Cronograma cronograma = new Cronograma()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .motivoAjuste(UPDATED_MOTIVO_AJUSTE)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA);
        return cronograma;
    }

    @BeforeEach
    public void initTest() {
        cronograma = createEntity(em);
    }

    @Test
    @Transactional
    public void createCronograma() throws Exception {
        int databaseSizeBeforeCreate = cronogramaRepository.findAll().size();

        // Create the Cronograma
        CronogramaDTO cronogramaDTO = cronogramaMapper.toDto(cronograma);
        restCronogramaMockMvc.perform(post("/api/cronogramas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaDTO)))
            .andExpect(status().isCreated());

        // Validate the Cronograma in the database
        List<Cronograma> cronogramaList = cronogramaRepository.findAll();
        assertThat(cronogramaList).hasSize(databaseSizeBeforeCreate + 1);
        Cronograma testCronograma = cronogramaList.get(cronogramaList.size() - 1);
        assertThat(testCronograma.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCronograma.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCronograma.getMotivoAjuste()).isEqualTo(DEFAULT_MOTIVO_AJUSTE);
        assertThat(testCronograma.isActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testCronograma.getFechaAlta()).isEqualTo(DEFAULT_FECHA_ALTA);

        // Validate the Cronograma in Elasticsearch
        verify(mockCronogramaSearchRepository, times(1)).save(testCronograma);
    }

    @Test
    @Transactional
    public void createCronogramaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cronogramaRepository.findAll().size();

        // Create the Cronograma with an existing ID
        cronograma.setId(1L);
        CronogramaDTO cronogramaDTO = cronogramaMapper.toDto(cronograma);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronogramaMockMvc.perform(post("/api/cronogramas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cronograma in the database
        List<Cronograma> cronogramaList = cronogramaRepository.findAll();
        assertThat(cronogramaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cronograma in Elasticsearch
        verify(mockCronogramaSearchRepository, times(0)).save(cronograma);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronogramaRepository.findAll().size();
        // set the field null
        cronograma.setNombre(null);

        // Create the Cronograma, which fails.
        CronogramaDTO cronogramaDTO = cronogramaMapper.toDto(cronograma);

        restCronogramaMockMvc.perform(post("/api/cronogramas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaDTO)))
            .andExpect(status().isBadRequest());

        List<Cronograma> cronogramaList = cronogramaRepository.findAll();
        assertThat(cronogramaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCronogramas() throws Exception {
        // Initialize the database
        cronogramaRepository.saveAndFlush(cronograma);

        // Get all the cronogramaList
        restCronogramaMockMvc.perform(get("/api/cronogramas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronograma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].motivoAjuste").value(hasItem(DEFAULT_MOTIVO_AJUSTE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(sameInstant(DEFAULT_FECHA_ALTA))));
    }
    
    @Test
    @Transactional
    public void getCronograma() throws Exception {
        // Initialize the database
        cronogramaRepository.saveAndFlush(cronograma);

        // Get the cronograma
        restCronogramaMockMvc.perform(get("/api/cronogramas/{id}", cronograma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cronograma.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.motivoAjuste").value(DEFAULT_MOTIVO_AJUSTE))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()))
            .andExpect(jsonPath("$.fechaAlta").value(sameInstant(DEFAULT_FECHA_ALTA)));
    }

    @Test
    @Transactional
    public void getNonExistingCronograma() throws Exception {
        // Get the cronograma
        restCronogramaMockMvc.perform(get("/api/cronogramas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCronograma() throws Exception {
        // Initialize the database
        cronogramaRepository.saveAndFlush(cronograma);

        int databaseSizeBeforeUpdate = cronogramaRepository.findAll().size();

        // Update the cronograma
        Cronograma updatedCronograma = cronogramaRepository.findById(cronograma.getId()).get();
        // Disconnect from session so that the updates on updatedCronograma are not directly saved in db
        em.detach(updatedCronograma);
        updatedCronograma
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .motivoAjuste(UPDATED_MOTIVO_AJUSTE)
            .activo(UPDATED_ACTIVO)
            .fechaAlta(UPDATED_FECHA_ALTA);
        CronogramaDTO cronogramaDTO = cronogramaMapper.toDto(updatedCronograma);

        restCronogramaMockMvc.perform(put("/api/cronogramas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaDTO)))
            .andExpect(status().isOk());

        // Validate the Cronograma in the database
        List<Cronograma> cronogramaList = cronogramaRepository.findAll();
        assertThat(cronogramaList).hasSize(databaseSizeBeforeUpdate);
        Cronograma testCronograma = cronogramaList.get(cronogramaList.size() - 1);
        assertThat(testCronograma.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCronograma.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCronograma.getMotivoAjuste()).isEqualTo(UPDATED_MOTIVO_AJUSTE);
        assertThat(testCronograma.isActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testCronograma.getFechaAlta()).isEqualTo(UPDATED_FECHA_ALTA);

        // Validate the Cronograma in Elasticsearch
        verify(mockCronogramaSearchRepository, times(1)).save(testCronograma);
    }

    @Test
    @Transactional
    public void updateNonExistingCronograma() throws Exception {
        int databaseSizeBeforeUpdate = cronogramaRepository.findAll().size();

        // Create the Cronograma
        CronogramaDTO cronogramaDTO = cronogramaMapper.toDto(cronograma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronogramaMockMvc.perform(put("/api/cronogramas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cronogramaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cronograma in the database
        List<Cronograma> cronogramaList = cronogramaRepository.findAll();
        assertThat(cronogramaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cronograma in Elasticsearch
        verify(mockCronogramaSearchRepository, times(0)).save(cronograma);
    }

    @Test
    @Transactional
    public void deleteCronograma() throws Exception {
        // Initialize the database
        cronogramaRepository.saveAndFlush(cronograma);

        int databaseSizeBeforeDelete = cronogramaRepository.findAll().size();

        // Delete the cronograma
        restCronogramaMockMvc.perform(delete("/api/cronogramas/{id}", cronograma.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cronograma> cronogramaList = cronogramaRepository.findAll();
        assertThat(cronogramaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cronograma in Elasticsearch
        verify(mockCronogramaSearchRepository, times(1)).deleteById(cronograma.getId());
    }

    @Test
    @Transactional
    public void searchCronograma() throws Exception {
        // Initialize the database
        cronogramaRepository.saveAndFlush(cronograma);
        when(mockCronogramaSearchRepository.search(queryStringQuery("id:" + cronograma.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cronograma), PageRequest.of(0, 1), 1));
        // Search the cronograma
        restCronogramaMockMvc.perform(get("/api/_search/cronogramas?query=id:" + cronograma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronograma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].motivoAjuste").value(hasItem(DEFAULT_MOTIVO_AJUSTE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(sameInstant(DEFAULT_FECHA_ALTA))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cronograma.class);
        Cronograma cronograma1 = new Cronograma();
        cronograma1.setId(1L);
        Cronograma cronograma2 = new Cronograma();
        cronograma2.setId(cronograma1.getId());
        assertThat(cronograma1).isEqualTo(cronograma2);
        cronograma2.setId(2L);
        assertThat(cronograma1).isNotEqualTo(cronograma2);
        cronograma1.setId(null);
        assertThat(cronograma1).isNotEqualTo(cronograma2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronogramaDTO.class);
        CronogramaDTO cronogramaDTO1 = new CronogramaDTO();
        cronogramaDTO1.setId(1L);
        CronogramaDTO cronogramaDTO2 = new CronogramaDTO();
        assertThat(cronogramaDTO1).isNotEqualTo(cronogramaDTO2);
        cronogramaDTO2.setId(cronogramaDTO1.getId());
        assertThat(cronogramaDTO1).isEqualTo(cronogramaDTO2);
        cronogramaDTO2.setId(2L);
        assertThat(cronogramaDTO1).isNotEqualTo(cronogramaDTO2);
        cronogramaDTO1.setId(null);
        assertThat(cronogramaDTO1).isNotEqualTo(cronogramaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cronogramaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cronogramaMapper.fromId(null)).isNull();
    }
}
