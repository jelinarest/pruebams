package scjn.gob.mx.web.rest;

import scjn.gob.mx.PruebamsApp;
import scjn.gob.mx.config.TestSecurityConfiguration;
import scjn.gob.mx.domain.Catalogo;
import scjn.gob.mx.repository.CatalogoRepository;
import scjn.gob.mx.repository.search.CatalogoSearchRepository;
import scjn.gob.mx.service.CatalogoService;
import scjn.gob.mx.service.dto.CatalogoDTO;
import scjn.gob.mx.service.mapper.CatalogoMapper;
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
 * Integration tests for the {@link CatalogoResource} REST controller.
 */
@SpringBootTest(classes = {PruebamsApp.class, TestSecurityConfiguration.class})
public class CatalogoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    @Autowired
    private CatalogoRepository catalogoRepository;

    @Autowired
    private CatalogoMapper catalogoMapper;

    @Autowired
    private CatalogoService catalogoService;

    /**
     * This repository is mocked in the scjn.gob.mx.repository.search test package.
     *
     * @see scjn.gob.mx.repository.search.CatalogoSearchRepositoryMockConfiguration
     */
    @Autowired
    private CatalogoSearchRepository mockCatalogoSearchRepository;

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

    private MockMvc restCatalogoMockMvc;

    private Catalogo catalogo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CatalogoResource catalogoResource = new CatalogoResource(catalogoService);
        this.restCatalogoMockMvc = MockMvcBuilders.standaloneSetup(catalogoResource)
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
    public static Catalogo createEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo()
            .nombre(DEFAULT_NOMBRE)
            .activo(DEFAULT_ACTIVO);
        return catalogo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogo createUpdatedEntity(EntityManager em) {
        Catalogo catalogo = new Catalogo()
            .nombre(UPDATED_NOMBRE)
            .activo(UPDATED_ACTIVO);
        return catalogo;
    }

    @BeforeEach
    public void initTest() {
        catalogo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatalogo() throws Exception {
        int databaseSizeBeforeCreate = catalogoRepository.findAll().size();

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);
        restCatalogoMockMvc.perform(post("/api/catalogos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isCreated());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate + 1);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCatalogo.isActivo()).isEqualTo(DEFAULT_ACTIVO);

        // Validate the Catalogo in Elasticsearch
        verify(mockCatalogoSearchRepository, times(1)).save(testCatalogo);
    }

    @Test
    @Transactional
    public void createCatalogoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catalogoRepository.findAll().size();

        // Create the Catalogo with an existing ID
        catalogo.setId(1L);
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogoMockMvc.perform(post("/api/catalogos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Catalogo in Elasticsearch
        verify(mockCatalogoSearchRepository, times(0)).save(catalogo);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogoRepository.findAll().size();
        // set the field null
        catalogo.setNombre(null);

        // Create the Catalogo, which fails.
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        restCatalogoMockMvc.perform(post("/api/catalogos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCatalogos() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        // Get all the catalogoList
        restCatalogoMockMvc.perform(get("/api/catalogos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        // Get the catalogo
        restCatalogoMockMvc.perform(get("/api/catalogos/{id}", catalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(catalogo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCatalogo() throws Exception {
        // Get the catalogo
        restCatalogoMockMvc.perform(get("/api/catalogos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Update the catalogo
        Catalogo updatedCatalogo = catalogoRepository.findById(catalogo.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogo are not directly saved in db
        em.detach(updatedCatalogo);
        updatedCatalogo
            .nombre(UPDATED_NOMBRE)
            .activo(UPDATED_ACTIVO);
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(updatedCatalogo);

        restCatalogoMockMvc.perform(put("/api/catalogos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isOk());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);
        Catalogo testCatalogo = catalogoList.get(catalogoList.size() - 1);
        assertThat(testCatalogo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCatalogo.isActivo()).isEqualTo(UPDATED_ACTIVO);

        // Validate the Catalogo in Elasticsearch
        verify(mockCatalogoSearchRepository, times(1)).save(testCatalogo);
    }

    @Test
    @Transactional
    public void updateNonExistingCatalogo() throws Exception {
        int databaseSizeBeforeUpdate = catalogoRepository.findAll().size();

        // Create the Catalogo
        CatalogoDTO catalogoDTO = catalogoMapper.toDto(catalogo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogoMockMvc.perform(put("/api/catalogos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(catalogoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogo in the database
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Catalogo in Elasticsearch
        verify(mockCatalogoSearchRepository, times(0)).save(catalogo);
    }

    @Test
    @Transactional
    public void deleteCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);

        int databaseSizeBeforeDelete = catalogoRepository.findAll().size();

        // Delete the catalogo
        restCatalogoMockMvc.perform(delete("/api/catalogos/{id}", catalogo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catalogo> catalogoList = catalogoRepository.findAll();
        assertThat(catalogoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Catalogo in Elasticsearch
        verify(mockCatalogoSearchRepository, times(1)).deleteById(catalogo.getId());
    }

    @Test
    @Transactional
    public void searchCatalogo() throws Exception {
        // Initialize the database
        catalogoRepository.saveAndFlush(catalogo);
        when(mockCatalogoSearchRepository.search(queryStringQuery("id:" + catalogo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(catalogo), PageRequest.of(0, 1), 1));
        // Search the catalogo
        restCatalogoMockMvc.perform(get("/api/_search/catalogos?query=id:" + catalogo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalogo.class);
        Catalogo catalogo1 = new Catalogo();
        catalogo1.setId(1L);
        Catalogo catalogo2 = new Catalogo();
        catalogo2.setId(catalogo1.getId());
        assertThat(catalogo1).isEqualTo(catalogo2);
        catalogo2.setId(2L);
        assertThat(catalogo1).isNotEqualTo(catalogo2);
        catalogo1.setId(null);
        assertThat(catalogo1).isNotEqualTo(catalogo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogoDTO.class);
        CatalogoDTO catalogoDTO1 = new CatalogoDTO();
        catalogoDTO1.setId(1L);
        CatalogoDTO catalogoDTO2 = new CatalogoDTO();
        assertThat(catalogoDTO1).isNotEqualTo(catalogoDTO2);
        catalogoDTO2.setId(catalogoDTO1.getId());
        assertThat(catalogoDTO1).isEqualTo(catalogoDTO2);
        catalogoDTO2.setId(2L);
        assertThat(catalogoDTO1).isNotEqualTo(catalogoDTO2);
        catalogoDTO1.setId(null);
        assertThat(catalogoDTO1).isNotEqualTo(catalogoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(catalogoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(catalogoMapper.fromId(null)).isNull();
    }
}
