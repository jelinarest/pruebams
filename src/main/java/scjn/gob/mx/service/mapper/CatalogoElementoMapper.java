package scjn.gob.mx.service.mapper;

import scjn.gob.mx.domain.*;
import scjn.gob.mx.service.dto.CatalogoElementoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogoElemento} and its DTO {@link CatalogoElementoDTO}.
 */
@Mapper(componentModel = "spring", uses = {CatalogoMapper.class})
public interface CatalogoElementoMapper extends EntityMapper<CatalogoElementoDTO, CatalogoElemento> {

    @Mapping(source = "catalogo.id", target = "catalogoId")
    @Mapping(source = "catalogo.nombre", target = "catalogoNombre")
    CatalogoElementoDTO toDto(CatalogoElemento catalogoElemento);

    @Mapping(source = "catalogoId", target = "catalogo")
    CatalogoElemento toEntity(CatalogoElementoDTO catalogoElementoDTO);

    default CatalogoElemento fromId(Long id) {
        if (id == null) {
            return null;
        }
        CatalogoElemento catalogoElemento = new CatalogoElemento();
        catalogoElemento.setId(id);
        return catalogoElemento;
    }
}
