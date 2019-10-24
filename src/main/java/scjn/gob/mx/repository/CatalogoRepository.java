package scjn.gob.mx.repository;
import scjn.gob.mx.domain.Catalogo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Catalogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {

}
