package scjn.gob.mx.repository;
import scjn.gob.mx.domain.CronogramaEtapa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CronogramaEtapa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronogramaEtapaRepository extends JpaRepository<CronogramaEtapa, Long> {

}
