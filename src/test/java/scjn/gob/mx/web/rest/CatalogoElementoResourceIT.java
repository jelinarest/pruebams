package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.CatalogoElemento;
import scjn.gob.mx.domain.Catalogo;
import scjn.gob.mx.repository.CatalogoElementoRepository;
import scjn.gob.mx.repository.search.CatalogoElementoSearchRepository;
import scjn.gob.mx.service.CatalogoElementoService;
import scjn.gob.mx.service.dto.CatalogoElementoDTO;
import scjn.gob.mx.service.mapper.CatalogoElementoMapper;
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
 * Integration tests for the {@link CatalogoElementoResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class CatalogoElementoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private CatalogoElementoRepository catalogoElementoRepository;

    @Autowired
    private CatalogoElementoMapper catalogoElementoMapper;

    @Autowired
    private CatalogoElementoService catalogoElementoService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.CatalogoElementoSearchRepositoryMockConfiguration
     */
    @Autowired
    private CatalogoElementoSearchRepository mockCatalogoElementoSearchRepository;

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

    private MockMvc restCatalogoElementoMockMvc;

    private CatalogoElemento catalogoElemento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CatalogoElementoResource catalogoElementoResource = new CatalogoElementoResource(catalogoElementoService);
        this.restCatalogoElementoMockMvc = MockMvcBuilders.standaloneSetup(catalogoElementoResource)
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
    public static CatalogoElemento createEntity(EntityManager em) {
        CatalogoElemento catalogoElemento = new CatalogoElemento()
            .nombre(DEFAULT_NOMBRE)
            .activo(DEFAULT_ACTIVO);
        // Add required entity
        Catalogo catalogo;
        if (TestUtil.findAll(em, Catalogo.class).isEmpty()) {
            catalogo = CatalogoResourceIT.createEntity(em);
            em.persist(catalogo);
            em.flush();
        } else {
            catalogo = TestUtil.findAll(em, Catalogo.class).get(0);
        }
        catalogoElemento.setCatalogo(catalogo);
        return catalogoElemento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogoElemento createUpdatedEntity(EntityManager em) {
        CatalogoElemento catalogoElemento = new CatalogoElemento()
            .nombre(UPDATED_NOMBRE)
            .activo(UPDATED_ACTIVO);
        // Add required entity
        Catalogo catalogo;
        if (TestUtil.findAll(em, Catalogo.class).isEmpty()) {
            catalogo = CatalogoResourceIT.createUpdatedEntity(em);
            em.persist(catalogo);
            em.flush();
        } else {
            catalogo = TestUtil.findAll(em, Catalogo.class).get(0);
        }
        catalogoElemento.setCatalogo(catalogo);
        return catalogoElemento;
    }

    @BeforeEach
    public void initTest() {
        catalogoElemento = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatalogoElemento() throws Exception {
        int databaseSizeBeforeCreate = catalogoElementoRepository.findAll().size();

        // Create the CatalogoElemento
        CatalogoElementoDTO catalogoElementoDTO = catalogoElementoMapper.toDto(catalogoElemento);
        restCatalogoElementoMockMvc.perform(post("/api/catalogo-elementos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoElementoDTO)))
            .andExpect(status().isCreated());

        // Validate the CatalogoElemento in the database
        List<CatalogoElemento> catalogoElementoList = catalogoElementoRepository.findAll();
        assertThat(catalogoElementoList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogoElemento testCatalogoElemento = catalogoElementoList.get(catalogoElementoList.size() - 1);
        assertThat(testCatalogoElemento.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogoElemento.isActivo()).isEqualTo(DEFAULT_ACTIVO);

        // Validate the CatalogoElemento in Elasticsearch
        verify(mockCatalogoElementoSearchRepository, times(1)).save(testCatalogoElemento);
    }

    @Test
    @Transactional
    public void createCatalogoElementoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catalogoElementoRepository.findAll().size();

        // Create the CatalogoElemento with an existing ID
        catalogoElemento.setId(1L);
        CatalogoElementoDTO catalogoElementoDTO = catalogoElementoMapper.toDto(catalogoElemento);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogoElementoMockMvc.perform(post("/api/catalogo-elementos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoElementoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatalogoElemento in the database
        List<CatalogoElemento> catalogoElementoList = catalogoElementoRepository.findAll();
        assertThat(catalogoElementoList).hasSize(databaseSizeBeforeCreate);

        // Validate the CatalogoElemento in Elasticsearch
        verify(mockCatalogoElementoSearchRepository, times(0)).save(catalogoElemento);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoElementoRepository.findAll().size();
        // set the field null
        catalogoElemento.setNombre(null);

        // Create the CatalogoElemento, which fails.
        CatalogoElementoDTO catalogoElementoDTO = catalogoElementoMapper.toDto(catalogoElemento);

        restCatalogoElementoMockMvc.perform(post("/api/catalogo-elementos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoElementoDTO)))
            .andExpect(status().isBadRequest());

        List<CatalogoElemento> catalogoElementoList = catalogoElementoRepository.findAll();
        assertThat(catalogoElementoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCatalogoElementos() throws Exception {
        // Initialize the database
        catalogoElementoRepository.saveAndFlush(catalogoElemento);

        // Get all the catalogoElementoList
        restCatalogoElementoMockMvc.perform(get("/api/catalogo-elementos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogoElemento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCatalogoElemento() throws Exception {
        // Initialize the database
        catalogoElementoRepository.saveAndFlush(catalogoElemento);

        // Get the catalogoElemento
        restCatalogoElementoMockMvc.perform(get("/api/catalogo-elementos/{id}", catalogoElemento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(catalogoElemento.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCatalogoElemento() throws Exception {
        // Get the catalogoElemento
        restCatalogoElementoMockMvc.perform(get("/api/catalogo-elementos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatalogoElemento() throws Exception {
        // Initialize the database
        catalogoElementoRepository.saveAndFlush(catalogoElemento);

        int databaseSizeBeforeUpdate = catalogoElementoRepository.findAll().size();

        // Update the catalogoElemento
        CatalogoElemento updatedCatalogoElemento = catalogoElementoRepository.findById(catalogoElemento.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogoElemento are not directly saved in db
        em.detach(updatedCatalogoElemento);
        updatedCatalogoElemento
            .nombre(UPDATED_NOMBRE)
            .activo(UPDATED_ACTIVO);
        CatalogoElementoDTO catalogoElementoDTO = catalogoElementoMapper.toDto(updatedCatalogoElemento);

        restCatalogoElementoMockMvc.perform(put("/api/catalogo-elementos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoElementoDTO)))
            .andExpect(status().isOk());

        // Validate the CatalogoElemento in the database
        List<CatalogoElemento> catalogoElementoList = catalogoElementoRepository.findAll();
        assertThat(catalogoElementoList).hasSize(databaseSizeBeforeUpdate);
        CatalogoElemento testCatalogoElemento = catalogoElementoList.get(catalogoElementoList.size() - 1);
        assertThat(testCatalogoElemento.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogoElemento.isActivo()).isEqualTo(UPDATED_ACTIVO);

        // Validate the CatalogoElemento in Elasticsearch
        verify(mockCatalogoElementoSearchRepository, times(1)).save(testCatalogoElemento);
    }

    @Test
    @Transactional
    public void updateNonExistingCatalogoElemento() throws Exception {
        int databaseSizeBeforeUpdate = catalogoElementoRepository.findAll().size();

        // Create the CatalogoElemento
        CatalogoElementoDTO catalogoElementoDTO = catalogoElementoMapper.toDto(catalogoElemento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoElementoMockMvc.perform(put("/api/catalogo-elementos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoElementoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CatalogoElemento in the database
        List<CatalogoElemento> catalogoElementoList = catalogoElementoRepository.findAll();
        assertThat(catalogoElementoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CatalogoElemento in Elasticsearch
        verify(mockCatalogoElementoSearchRepository, times(0)).save(catalogoElemento);
    }

    @Test
    @Transactional
    public void deleteCatalogoElemento() throws Exception {
        // Initialize the database
        catalogoElementoRepository.saveAndFlush(catalogoElemento);

        int databaseSizeBeforeDelete = catalogoElementoRepository.findAll().size();

        // Delete the catalogoElemento
        restCatalogoElementoMockMvc.perform(delete("/api/catalogo-elementos/{id}", catalogoElemento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogoElemento> catalogoElementoList = catalogoElementoRepository.findAll();
        assertThat(catalogoElementoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CatalogoElemento in Elasticsearch
        verify(mockCatalogoElementoSearchRepository, times(1)).deleteById(catalogoElemento.getId());
    }

    @Test
    @Transactional
    public void searchCatalogoElemento() throws Exception {
        // Initialize the database
        catalogoElementoRepository.saveAndFlush(catalogoElemento);
        when(mockCatalogoElementoSearchRepository.search(queryStringQuery("id:" + catalogoElemento.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(catalogoElemento), PageRequest.of(0, 1), 1));
        // Search the catalogoElemento
        restCatalogoElementoMockMvc.perform(get("/api/_search/catalogo-elementos?query=id:" + catalogoElemento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogoElemento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogoElemento.class);
        CatalogoElemento catalogoElemento1 = new CatalogoElemento();
        catalogoElemento1.setId(1L);
        CatalogoElemento catalogoElemento2 = new CatalogoElemento();
        catalogoElemento2.setId(catalogoElemento1.getId());
        assertThat(catalogoElemento1).isEqualTo(catalogoElemento2);
        catalogoElemento2.setId(2L);
        assertThat(catalogoElemento1).isNotEqualTo(catalogoElemento2);
        catalogoElemento1.setId(null);
        assertThat(catalogoElemento1).isNotEqualTo(catalogoElemento2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogoElementoDTO.class);
        CatalogoElementoDTO catalogoElementoDTO1 = new CatalogoElementoDTO();
        catalogoElementoDTO1.setId(1L);
        CatalogoElementoDTO catalogoElementoDTO2 = new CatalogoElementoDTO();
        assertThat(catalogoElementoDTO1).isNotEqualTo(catalogoElementoDTO2);
        catalogoElementoDTO2.setId(catalogoElementoDTO1.getId());
        assertThat(catalogoElementoDTO1).isEqualTo(catalogoElementoDTO2);
        catalogoElementoDTO2.setId(2L);
        assertThat(catalogoElementoDTO1).isNotEqualTo(catalogoElementoDTO2);
        catalogoElementoDTO1.setId(null);
        assertThat(catalogoElementoDTO1).isNotEqualTo(catalogoElementoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(catalogoElementoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(catalogoElementoMapper.fromId(null)).isNull();
    }
}
