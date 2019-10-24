package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.TipoDato;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TipoDato} entity.
 */
public interface TipoDatoSearchRepository extends ElasticsearchRepository<TipoDato, Long> {
}
