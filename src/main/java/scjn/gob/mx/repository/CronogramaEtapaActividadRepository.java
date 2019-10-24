package scjn.gob.mx.repository;
import scjn.gob.mx.domain.CronogramaEtapaActividad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CronogramaEtapaActividad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronogramaEtapaActividadRepository extends JpaRepository<CronogramaEtapaActividad, Long> {

}
