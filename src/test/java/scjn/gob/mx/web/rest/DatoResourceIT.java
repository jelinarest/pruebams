package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.Dato;
import scjn.gob.mx.repository.DatoRepository;
import scjn.gob.mx.repository.search.DatoSearchRepository;
import scjn.gob.mx.service.DatoService;
import scjn.gob.mx.service.dto.DatoDTO;
import scjn.gob.mx.service.mapper.DatoMapper;
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
 * Integration tests for the {@link DatoResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class DatoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private DatoRepository datoRepository;

    @Autowired
    private DatoMapper datoMapper;

    @Autowired
    private DatoService datoService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.DatoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DatoSearchRepository mockDatoSearchRepository;

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

    private MockMvc restDatoMockMvc;

    private Dato dato;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DatoResource datoResource = new DatoResource(datoService);
        this.restDatoMockMvc = MockMvcBuilders.standaloneSetup(datoResource)
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
    public static Dato createEntity(EntityManager em) {
        Dato dato = new Dato()
            .nombre(DEFAULT_NOMBRE);
        return dato;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dato createUpdatedEntity(EntityManager em) {
        Dato dato = new Dato()
            .nombre(UPDATED_NOMBRE);
        return dato;
    }

    @BeforeEach
    public void initTest() {
        dato = createEntity(em);
    }

    @Test
    @Transactional
    public void createDato() throws Exception {
        int databaseSizeBeforeCreate = datoRepository.findAll().size();

        // Create the Dato
        DatoDTO datoDTO = datoMapper.toDto(dato);
        restDatoMockMvc.perform(post("/api/datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datoDTO)))
            .andExpect(status().isCreated());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeCreate + 1);
        Dato testDato = datoList.get(datoList.size() - 1);
        assertThat(testDato.getNombre()).isEqualTo(DEFAULT_NOMBRE);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(1)).save(testDato);
    }

    @Test
    @Transactional
    public void createDatoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = datoRepository.findAll().size();

        // Create the Dato with an existing ID
        dato.setId(1L);
        DatoDTO datoDTO = datoMapper.toDto(dato);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatoMockMvc.perform(post("/api/datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(0)).save(dato);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = datoRepository.findAll().size();
        // set the field null
        dato.setNombre(null);

        // Create the Dato, which fails.
        DatoDTO datoDTO = datoMapper.toDto(dato);

        restDatoMockMvc.perform(post("/api/datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datoDTO)))
            .andExpect(status().isBadRequest());

        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDatoes() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);

        // Get all the datoList
        restDatoMockMvc.perform(get("/api/datoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dato.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }
    
    @Test
    @Transactional
    public void getDato() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);

        // Get the dato
        restDatoMockMvc.perform(get("/api/datoes/{id}", dato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dato.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    public void getNonExistingDato() throws Exception {
        // Get the dato
        restDatoMockMvc.perform(get("/api/datoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDato() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);

        int databaseSizeBeforeUpdate = datoRepository.findAll().size();

        // Update the dato
        Dato updatedDato = datoRepository.findById(dato.getId()).get();
        // Disconnect from session so that the updates on updatedDato are not directly saved in db
        em.detach(updatedDato);
        updatedDato
            .nombre(UPDATED_NOMBRE);
        DatoDTO datoDTO = datoMapper.toDto(updatedDato);

        restDatoMockMvc.perform(put("/api/datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datoDTO)))
            .andExpect(status().isOk());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeUpdate);
        Dato testDato = datoList.get(datoList.size() - 1);
        assertThat(testDato.getNombre()).isEqualTo(UPDATED_NOMBRE);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(1)).save(testDato);
    }

    @Test
    @Transactional
    public void updateNonExistingDato() throws Exception {
        int databaseSizeBeforeUpdate = datoRepository.findAll().size();

        // Create the Dato
        DatoDTO datoDTO = datoMapper.toDto(dato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoMockMvc.perform(put("/api/datoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(datoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dato in the database
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(0)).save(dato);
    }

    @Test
    @Transactional
    public void deleteDato() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);

        int databaseSizeBeforeDelete = datoRepository.findAll().size();

        // Delete the dato
        restDatoMockMvc.perform(delete("/api/datoes/{id}", dato.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dato> datoList = datoRepository.findAll();
        assertThat(datoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dato in Elasticsearch
        verify(mockDatoSearchRepository, times(1)).deleteById(dato.getId());
    }

    @Test
    @Transactional
    public void searchDato() throws Exception {
        // Initialize the database
        datoRepository.saveAndFlush(dato);
        when(mockDatoSearchRepository.search(queryStringQuery("id:" + dato.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dato), PageRequest.of(0, 1), 1));
        // Search the dato
        restDatoMockMvc.perform(get("/api/_search/datoes?query=id:" + dato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dato.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dato.class);
        Dato dato1 = new Dato();
        dato1.setId(1L);
        Dato dato2 = new Dato();
        dato2.setId(dato1.getId());
        assertThat(dato1).isEqualTo(dato2);
        dato2.setId(2L);
        assertThat(dato1).isNotEqualTo(dato2);
        dato1.setId(null);
        assertThat(dato1).isNotEqualTo(dato2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoDTO.class);
        DatoDTO datoDTO1 = new DatoDTO();
        datoDTO1.setId(1L);
        DatoDTO datoDTO2 = new DatoDTO();
        assertThat(datoDTO1).isNotEqualTo(datoDTO2);
        datoDTO2.setId(datoDTO1.getId());
        assertThat(datoDTO1).isEqualTo(datoDTO2);
        datoDTO2.setId(2L);
        assertThat(datoDTO1).isNotEqualTo(datoDTO2);
        datoDTO1.setId(null);
        assertThat(datoDTO1).isNotEqualTo(datoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(datoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(datoMapper.fromId(null)).isNull();
    }
}
