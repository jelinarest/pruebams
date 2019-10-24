package scjn.gob.mx.repository;
import scjn.gob.mx.domain.Plantilla;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Plantilla entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long> {

}
