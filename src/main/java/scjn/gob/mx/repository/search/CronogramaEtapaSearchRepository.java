package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.CronogramaEtapa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CronogramaEtapa} entity.
 */
public interface CronogramaEtapaSearchRepository extends ElasticsearchRepository<CronogramaEtapa, Long> {
}
