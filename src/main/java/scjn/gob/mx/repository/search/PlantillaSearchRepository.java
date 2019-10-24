package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.Plantilla;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Plantilla} entity.
 */
public interface PlantillaSearchRepository extends ElasticsearchRepository<Plantilla, Long> {
}
