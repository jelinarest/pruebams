package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.Catalogo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Catalogo} entity.
 */
public interface CatalogoSearchRepository extends ElasticsearchRepository<Catalogo, Long> {
}
