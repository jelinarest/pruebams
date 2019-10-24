package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.Plantilla;
import scjn.gob.mx.domain.TipoPlantilla;
import scjn.gob.mx.repository.PlantillaRepository;
import scjn.gob.mx.repository.search.PlantillaSearchRepository;
import scjn.gob.mx.service.PlantillaService;
import scjn.gob.mx.service.dto.PlantillaDTO;
import scjn.gob.mx.service.mapper.PlantillaMapper;
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
 * Integration tests for the {@link PlantillaResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class PlantillaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_COLUMNAS = 1;
    private static final Integer UPDATED_NUMERO_COLUMNAS = 2;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private PlantillaRepository plantillaRepository;

    @Autowired
    private PlantillaMapper plantillaMapper;

    @Autowired
    private PlantillaService plantillaService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.PlantillaSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlantillaSearchRepository mockPlantillaSearchRepository;

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

    private MockMvc restPlantillaMockMvc;

    private Plantilla plantilla;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlantillaResource plantillaResource = new PlantillaResource(plantillaService);
        this.restPlantillaMockMvc = MockMvcBuilders.standaloneSetup(plantillaResource)
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
    public static Plantilla createEntity(EntityManager em) {
        Plantilla plantilla = new Plantilla()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .numeroColumnas(DEFAULT_NUMERO_COLUMNAS)
            .activo(DEFAULT_ACTIVO);
        // Add required entity
        TipoPlantilla tipoPlantilla;
        if (TestUtil.findAll(em, TipoPlantilla.class).isEmpty()) {
            tipoPlantilla = TipoPlantillaResourceIT.createEntity(em);
            em.persist(tipoPlantilla);
            em.flush();
        } else {
            tipoPlantilla = TestUtil.findAll(em, TipoPlantilla.class).get(0);
        }
        plantilla.setTipoPlantilla(tipoPlantilla);
        return plantilla;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plantilla createUpdatedEntity(EntityManager em) {
        Plantilla plantilla = new Plantilla()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .numeroColumnas(UPDATED_NUMERO_COLUMNAS)
            .activo(UPDATED_ACTIVO);
        // Add required entity
        TipoPlantilla tipoPlantilla;
        if (TestUtil.findAll(em, TipoPlantilla.class).isEmpty()) {
            tipoPlantilla = TipoPlantillaResourceIT.createUpdatedEntity(em);
            em.persist(tipoPlantilla);
            em.flush();
        } else {
            tipoPlantilla = TestUtil.findAll(em, TipoPlantilla.class).get(0);
        }
        plantilla.setTipoPlantilla(tipoPlantilla);
        return plantilla;
    }

    @BeforeEach
    public void initTest() {
        plantilla = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlantilla() throws Exception {
        int databaseSizeBeforeCreate = plantillaRepository.findAll().size();

        // Create the Plantilla
        PlantillaDTO plantillaDTO = plantillaMapper.toDto(plantilla);
        restPlantillaMockMvc.perform(post("/api/plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plantillaDTO)))
            .andExpect(status().isCreated());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeCreate + 1);
        Plantilla testPlantilla = plantillaList.get(plantillaList.size() - 1);
        assertThat(testPlantilla.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPlantilla.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPlantilla.getNumeroColumnas()).isEqualTo(DEFAULT_NUMERO_COLUMNAS);
        assertThat(testPlantilla.isActivo()).isEqualTo(DEFAULT_ACTIVO);

        // Validate the Plantilla in Elasticsearch
        verify(mockPlantillaSearchRepository, times(1)).save(testPlantilla);
    }

    @Test
    @Transactional
    public void createPlantillaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plantillaRepository.findAll().size();

        // Create the Plantilla with an existing ID
        plantilla.setId(1L);
        PlantillaDTO plantillaDTO = plantillaMapper.toDto(plantilla);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantillaMockMvc.perform(post("/api/plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plantillaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Plantilla in Elasticsearch
        verify(mockPlantillaSearchRepository, times(0)).save(plantilla);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantillaRepository.findAll().size();
        // set the field null
        plantilla.setNombre(null);

        // Create the Plantilla, which fails.
        PlantillaDTO plantillaDTO = plantillaMapper.toDto(plantilla);

        restPlantillaMockMvc.perform(post("/api/plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plantillaDTO)))
            .andExpect(status().isBadRequest());

        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlantillas() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get all the plantillaList
        restPlantillaMockMvc.perform(get("/api/plantillas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantilla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].numeroColumnas").value(hasItem(DEFAULT_NUMERO_COLUMNAS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        // Get the plantilla
        restPlantillaMockMvc.perform(get("/api/plantillas/{id}", plantilla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plantilla.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.numeroColumnas").value(DEFAULT_NUMERO_COLUMNAS))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlantilla() throws Exception {
        // Get the plantilla
        restPlantillaMockMvc.perform(get("/api/plantillas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();

        // Update the plantilla
        Plantilla updatedPlantilla = plantillaRepository.findById(plantilla.getId()).get();
        // Disconnect from session so that the updates on updatedPlantilla are not directly saved in db
        em.detach(updatedPlantilla);
        updatedPlantilla
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .numeroColumnas(UPDATED_NUMERO_COLUMNAS)
            .activo(UPDATED_ACTIVO);
        PlantillaDTO plantillaDTO = plantillaMapper.toDto(updatedPlantilla);

        restPlantillaMockMvc.perform(put("/api/plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plantillaDTO)))
            .andExpect(status().isOk());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);
        Plantilla testPlantilla = plantillaList.get(plantillaList.size() - 1);
        assertThat(testPlantilla.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPlantilla.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPlantilla.getNumeroColumnas()).isEqualTo(UPDATED_NUMERO_COLUMNAS);
        assertThat(testPlantilla.isActivo()).isEqualTo(UPDATED_ACTIVO);

        // Validate the Plantilla in Elasticsearch
        verify(mockPlantillaSearchRepository, times(1)).save(testPlantilla);
    }

    @Test
    @Transactional
    public void updateNonExistingPlantilla() throws Exception {
        int databaseSizeBeforeUpdate = plantillaRepository.findAll().size();

        // Create the Plantilla
        PlantillaDTO plantillaDTO = plantillaMapper.toDto(plantilla);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantillaMockMvc.perform(put("/api/plantillas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plantillaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plantilla in the database
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Plantilla in Elasticsearch
        verify(mockPlantillaSearchRepository, times(0)).save(plantilla);
    }

    @Test
    @Transactional
    public void deletePlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);

        int databaseSizeBeforeDelete = plantillaRepository.findAll().size();

        // Delete the plantilla
        restPlantillaMockMvc.perform(delete("/api/plantillas/{id}", plantilla.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plantilla> plantillaList = plantillaRepository.findAll();
        assertThat(plantillaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Plantilla in Elasticsearch
        verify(mockPlantillaSearchRepository, times(1)).deleteById(plantilla.getId());
    }

    @Test
    @Transactional
    public void searchPlantilla() throws Exception {
        // Initialize the database
        plantillaRepository.saveAndFlush(plantilla);
        when(mockPlantillaSearchRepository.search(queryStringQuery("id:" + plantilla.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(plantilla), PageRequest.of(0, 1), 1));
        // Search the plantilla
        restPlantillaMockMvc.perform(get("/api/_search/plantillas?query=id:" + plantilla.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantilla.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].numeroColumnas").value(hasItem(DEFAULT_NUMERO_COLUMNAS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plantilla.class);
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setId(1L);
        Plantilla plantilla2 = new Plantilla();
        plantilla2.setId(plantilla1.getId());
        assertThat(plantilla1).isEqualTo(plantilla2);
        plantilla2.setId(2L);
        assertThat(plantilla1).isNotEqualTo(plantilla2);
        plantilla1.setId(null);
        assertThat(plantilla1).isNotEqualTo(plantilla2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantillaDTO.class);
        PlantillaDTO plantillaDTO1 = new PlantillaDTO();
        plantillaDTO1.setId(1L);
        PlantillaDTO plantillaDTO2 = new PlantillaDTO();
        assertThat(plantillaDTO1).isNotEqualTo(plantillaDTO2);
        plantillaDTO2.setId(plantillaDTO1.getId());
        assertThat(plantillaDTO1).isEqualTo(plantillaDTO2);
        plantillaDTO2.setId(2L);
        assertThat(plantillaDTO1).isNotEqualTo(plantillaDTO2);
        plantillaDTO1.setId(null);
        assertThat(plantillaDTO1).isNotEqualTo(plantillaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(plantillaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(plantillaMapper.fromId(null)).isNull();
    }
}
