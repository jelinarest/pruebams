package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.Campo;
import scjn.gob.mx.domain.Dato;
import scjn.gob.mx.repository.CampoRepository;
import scjn.gob.mx.repository.search.CampoSearchRepository;
import scjn.gob.mx.service.CampoService;
import scjn.gob.mx.service.dto.CampoDTO;
import scjn.gob.mx.service.mapper.CampoMapper;
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
 * Integration tests for the {@link CampoResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class CampoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MULTI_SELECCION = false;
    private static final Boolean UPDATED_MULTI_SELECCION = true;

    private static final Boolean DEFAULT_REQUERIDO = false;
    private static final Boolean UPDATED_REQUERIDO = true;

    private static final Integer DEFAULT_LONGITUD = 1;
    private static final Integer UPDATED_LONGITUD = 2;

    private static final Boolean DEFAULT_DEPENDIENTE_VISIBILIDAD = false;
    private static final Boolean UPDATED_DEPENDIENTE_VISIBILIDAD = true;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private CampoMapper campoMapper;

    @Autowired
    private CampoService campoService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.CampoSearchRepositoryMockConfiguration
     */
    @Autowired
    private CampoSearchRepository mockCampoSearchRepository;

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

    private MockMvc restCampoMockMvc;

    private Campo campo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampoResource campoResource = new CampoResource(campoService);
        this.restCampoMockMvc = MockMvcBuilders.standaloneSetup(campoResource)
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
    public static Campo createEntity(EntityManager em) {
        Campo campo = new Campo()
            .nombre(DEFAULT_NOMBRE)
            .multiSeleccion(DEFAULT_MULTI_SELECCION)
            .requerido(DEFAULT_REQUERIDO)
            .longitud(DEFAULT_LONGITUD)
            .dependienteVisibilidad(DEFAULT_DEPENDIENTE_VISIBILIDAD);
        // Add required entity
        Dato dato;
        if (TestUtil.findAll(em, Dato.class).isEmpty()) {
            dato = DatoResourceIT.createEntity(em);
            em.persist(dato);
            em.flush();
        } else {
            dato = TestUtil.findAll(em, Dato.class).get(0);
        }
        campo.setDato(dato);
        return campo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campo createUpdatedEntity(EntityManager em) {
        Campo campo = new Campo()
            .nombre(UPDATED_NOMBRE)
            .multiSeleccion(UPDATED_MULTI_SELECCION)
            .requerido(UPDATED_REQUERIDO)
            .longitud(UPDATED_LONGITUD)
            .dependienteVisibilidad(UPDATED_DEPENDIENTE_VISIBILIDAD);
        // Add required entity
        Dato dato;
        if (TestUtil.findAll(em, Dato.class).isEmpty()) {
            dato = DatoResourceIT.createUpdatedEntity(em);
            em.persist(dato);
            em.flush();
        } else {
            dato = TestUtil.findAll(em, Dato.class).get(0);
        }
        campo.setDato(dato);
        return campo;
    }

    @BeforeEach
    public void initTest() {
        campo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampo() throws Exception {
        int databaseSizeBeforeCreate = campoRepository.findAll().size();

        // Create the Campo
        CampoDTO campoDTO = campoMapper.toDto(campo);
        restCampoMockMvc.perform(post("/api/campos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campoDTO)))
            .andExpect(status().isCreated());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeCreate + 1);
        Campo testCampo = campoList.get(campoList.size() - 1);
        assertThat(testCampo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCampo.isMultiSeleccion()).isEqualTo(DEFAULT_MULTI_SELECCION);
        assertThat(testCampo.isRequerido()).isEqualTo(DEFAULT_REQUERIDO);
        assertThat(testCampo.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testCampo.isDependienteVisibilidad()).isEqualTo(DEFAULT_DEPENDIENTE_VISIBILIDAD);

        // Validate the Campo in Elasticsearch
        verify(mockCampoSearchRepository, times(1)).save(testCampo);
    }

    @Test
    @Transactional
    public void createCampoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campoRepository.findAll().size();

        // Create the Campo with an existing ID
        campo.setId(1L);
        CampoDTO campoDTO = campoMapper.toDto(campo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampoMockMvc.perform(post("/api/campos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Campo in Elasticsearch
        verify(mockCampoSearchRepository, times(0)).save(campo);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = campoRepository.findAll().size();
        // set the field null
        campo.setNombre(null);

        // Create the Campo, which fails.
        CampoDTO campoDTO = campoMapper.toDto(campo);

        restCampoMockMvc.perform(post("/api/campos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campoDTO)))
            .andExpect(status().isBadRequest());

        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCampos() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        // Get all the campoList
        restCampoMockMvc.perform(get("/api/campos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].multiSeleccion").value(hasItem(DEFAULT_MULTI_SELECCION.booleanValue())))
            .andExpect(jsonPath("$.[*].requerido").value(hasItem(DEFAULT_REQUERIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD)))
            .andExpect(jsonPath("$.[*].dependienteVisibilidad").value(hasItem(DEFAULT_DEPENDIENTE_VISIBILIDAD.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        // Get the campo
        restCampoMockMvc.perform(get("/api/campos/{id}", campo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.multiSeleccion").value(DEFAULT_MULTI_SELECCION.booleanValue()))
            .andExpect(jsonPath("$.requerido").value(DEFAULT_REQUERIDO.booleanValue()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD))
            .andExpect(jsonPath("$.dependienteVisibilidad").value(DEFAULT_DEPENDIENTE_VISIBILIDAD.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCampo() throws Exception {
        // Get the campo
        restCampoMockMvc.perform(get("/api/campos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        int databaseSizeBeforeUpdate = campoRepository.findAll().size();

        // Update the campo
        Campo updatedCampo = campoRepository.findById(campo.getId()).get();
        // Disconnect from session so that the updates on updatedCampo are not directly saved in db
        em.detach(updatedCampo);
        updatedCampo
            .nombre(UPDATED_NOMBRE)
            .multiSeleccion(UPDATED_MULTI_SELECCION)
            .requerido(UPDATED_REQUERIDO)
            .longitud(UPDATED_LONGITUD)
            .dependienteVisibilidad(UPDATED_DEPENDIENTE_VISIBILIDAD);
        CampoDTO campoDTO = campoMapper.toDto(updatedCampo);

        restCampoMockMvc.perform(put("/api/campos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campoDTO)))
            .andExpect(status().isOk());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeUpdate);
        Campo testCampo = campoList.get(campoList.size() - 1);
        assertThat(testCampo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCampo.isMultiSeleccion()).isEqualTo(UPDATED_MULTI_SELECCION);
        assertThat(testCampo.isRequerido()).isEqualTo(UPDATED_REQUERIDO);
        assertThat(testCampo.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testCampo.isDependienteVisibilidad()).isEqualTo(UPDATED_DEPENDIENTE_VISIBILIDAD);

        // Validate the Campo in Elasticsearch
        verify(mockCampoSearchRepository, times(1)).save(testCampo);
    }

    @Test
    @Transactional
    public void updateNonExistingCampo() throws Exception {
        int databaseSizeBeforeUpdate = campoRepository.findAll().size();

        // Create the Campo
        CampoDTO campoDTO = campoMapper.toDto(campo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampoMockMvc.perform(put("/api/campos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Campo in Elasticsearch
        verify(mockCampoSearchRepository, times(0)).save(campo);
    }

    @Test
    @Transactional
    public void deleteCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        int databaseSizeBeforeDelete = campoRepository.findAll().size();

        // Delete the campo
        restCampoMockMvc.perform(delete("/api/campos/{id}", campo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Campo in Elasticsearch
        verify(mockCampoSearchRepository, times(1)).deleteById(campo.getId());
    }

    @Test
    @Transactional
    public void searchCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);
        when(mockCampoSearchRepository.search(queryStringQuery("id:" + campo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(campo), PageRequest.of(0, 1), 1));
        // Search the campo
        restCampoMockMvc.perform(get("/api/_search/campos?query=id:" + campo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].multiSeleccion").value(hasItem(DEFAULT_MULTI_SELECCION.booleanValue())))
            .andExpect(jsonPath("$.[*].requerido").value(hasItem(DEFAULT_REQUERIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD)))
            .andExpect(jsonPath("$.[*].dependienteVisibilidad").value(hasItem(DEFAULT_DEPENDIENTE_VISIBILIDAD.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campo.class);
        Campo campo1 = new Campo();
        campo1.setId(1L);
        Campo campo2 = new Campo();
        campo2.setId(campo1.getId());
        assertThat(campo1).isEqualTo(campo2);
        campo2.setId(2L);
        assertThat(campo1).isNotEqualTo(campo2);
        campo1.setId(null);
        assertThat(campo1).isNotEqualTo(campo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampoDTO.class);
        CampoDTO campoDTO1 = new CampoDTO();
        campoDTO1.setId(1L);
        CampoDTO campoDTO2 = new CampoDTO();
        assertThat(campoDTO1).isNotEqualTo(campoDTO2);
        campoDTO2.setId(campoDTO1.getId());
        assertThat(campoDTO1).isEqualTo(campoDTO2);
        campoDTO2.setId(2L);
        assertThat(campoDTO1).isNotEqualTo(campoDTO2);
        campoDTO1.setId(null);
        assertThat(campoDTO1).isNotEqualTo(campoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(campoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(campoMapper.fromId(null)).isNull();
    }
}
