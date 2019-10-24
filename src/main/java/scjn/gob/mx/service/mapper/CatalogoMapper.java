package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.CatalogoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Catalogo} and its DTO {@link CatalogoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatalogoMapper extends EntityMapper<CatalogoDTO, Catalogo> {


    @Mapping(target = "catalogoElementos", ignore = true)
    @Mapping(target = "removeCatalogoElemento", ignore = true)
    @Mapping(target = "campos", ignore = true)
    @Mapping(target = "removeCampo", ignore = true)
    Catalogo toEntity(CatalogoDTO catalogoDTO);

    default Catalogo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Catalogo catalogo = new Catalogo();
        catalogo.setId(id);
        return catalogo;
    }
}
