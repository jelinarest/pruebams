package scjn.gob.mx.repository;
import scjn.gob.mx.domain.CatalogoElemento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CatalogoElemento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogoElementoRepository extends JpaRepository<CatalogoElemento, Long> {

}
