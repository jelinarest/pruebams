package scjn.gob.mx.repository;
import scjn.gob.mx.domain.TipoDato;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TipoDato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoDatoRepository extends JpaRepository<TipoDato, Long> {

}
