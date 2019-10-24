package scjn.gob.mx.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link DatoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DatoSearchRepositoryMockConfiguration {

    @MockBean
    private DatoSearchRepository mockDatoSearchRepository;

}
