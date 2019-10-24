package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.Dato;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Dato} entity.
 */
public interface DatoSearchRepository extends ElasticsearchRepository<Dato, Long> {
}
