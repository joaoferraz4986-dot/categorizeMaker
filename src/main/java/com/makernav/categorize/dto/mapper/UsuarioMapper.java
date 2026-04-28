package com.makernav.categorize.dto.mapper;

import com.makernav.categorize.dto.UsuarioDTO;
import com.makernav.categorize.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "senha", ignore = true)
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);

    @Mapping(target = "idUsuario", ignore = true)
    void updateEntityFromDTO(UsuarioDTO usuarioDTO, @MappingTarget Usuario usuario);
}
