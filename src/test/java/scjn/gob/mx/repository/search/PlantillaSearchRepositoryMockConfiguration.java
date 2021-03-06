package scjn.gob.mx.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PlantillaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PlantillaSearchRepositoryMockConfiguration {

    @MockBean
    private PlantillaSearchRepository mockPlantillaSearchRepository;

}
