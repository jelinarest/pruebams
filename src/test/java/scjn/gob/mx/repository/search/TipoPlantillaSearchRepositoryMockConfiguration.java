package scjn.gob.mx.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TipoPlantillaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TipoPlantillaSearchRepositoryMockConfiguration {

    @MockBean
    private TipoPlantillaSearchRepository mockTipoPlantillaSearchRepository;

}
