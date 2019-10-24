package scjn.gob.mx.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CatalogoElementoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CatalogoElementoSearchRepositoryMockConfiguration {

    @MockBean
    private CatalogoElementoSearchRepository mockCatalogoElementoSearchRepository;

}
