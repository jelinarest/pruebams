package scjn.gob.mx.repository;
import scjn.gob.mx.domain.Cronograma;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cronograma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {

}
