package scjn.gob.mx.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CronogramaEtapaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CronogramaEtapaSearchRepositoryMockConfiguration {

    @MockBean
    private CronogramaEtapaSearchRepository mockCronogramaEtapaSearchRepository;

}
