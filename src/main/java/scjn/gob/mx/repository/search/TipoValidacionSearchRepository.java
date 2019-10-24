package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.TipoValidacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TipoValidacion} entity.
 */
public interface TipoValidacionSearchRepository extends ElasticsearchRepository<TipoValidacion, Long> {
}
