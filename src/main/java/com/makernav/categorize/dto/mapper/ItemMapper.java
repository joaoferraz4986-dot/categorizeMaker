package com.makernav.categorize.dto.mapper;

import com.makernav.categorize.dto.ItemRequestDTO;
import com.makernav.categorize.dto.ItemResponseDTO;
import com.makernav.categorize.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper( componentModel = "spring" )
public interface ItemMapper {

    @Mapping( source = "idItem", target = "id" )
    ItemResponseDTO toResponseDTO( Item item );

    @Mapping( target = "idItem", ignore = true )
    Item toEntity( ItemRequestDTO itemRequestDTO );

    @Mapping( target = "idItem", ignore = true )
    void updateEntityFromDTO( ItemRequestDTO itemRequestDTO, @MappingTarget Item item );
}
