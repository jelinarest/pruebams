package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.Campo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Campo} entity.
 */
public interface CampoSearchRepository extends ElasticsearchRepository<Campo, Long> {
}
