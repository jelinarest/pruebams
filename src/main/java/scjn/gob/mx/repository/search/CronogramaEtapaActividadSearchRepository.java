package scjn.gob.mx.repository.search;
import scjn.gob.mx.domain.CronogramaEtapaActividad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CronogramaEtapaActividad} entity.
 */
public interface CronogramaEtapaActividadSearchRepository extends ElasticsearchRepository<CronogramaEtapaActividad, Long> {
}
