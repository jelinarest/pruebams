package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.TipoPlantilla;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TipoPlantilla} entity.
 */
public interface TipoPlantillaSearchRepository extends ElasticsearchRepository<TipoPlantilla, Long> {
}
