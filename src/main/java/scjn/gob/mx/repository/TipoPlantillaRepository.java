package scjn.gob.mx.repository;
import scjn.gob.mx.domain.TipoPlantilla;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoPlantilla entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoPlantillaRepository extends JpaRepository<TipoPlantilla, Long> {

}
