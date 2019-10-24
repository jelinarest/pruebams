package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.CatalogoElemento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CatalogoElemento} entity.
 */
public interface CatalogoElementoSearchRepository extends ElasticsearchRepository<CatalogoElemento, Long> {
}
