package scjn.gob.mx.repository;
import scjn.gob.mx.domain.Campo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Campo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampoRepository extends JpaRepository<Campo, Long> {

}
