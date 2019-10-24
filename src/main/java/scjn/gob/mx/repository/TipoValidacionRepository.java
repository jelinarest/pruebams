package scjn.gob.mx.repository;
import scjn.gob.mx.domain.TipoValidacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoValidacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoValidacionRepository extends JpaRepository<TipoValidacion, Long> {

}
