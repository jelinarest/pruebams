package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.Cronograma;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Cronograma} entity.
 */
public interface CronogramaSearchRepository extends ElasticsearchRepository<Cronograma, Long> {
}
