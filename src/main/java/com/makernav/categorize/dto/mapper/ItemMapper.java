package com.makernav.categorize.dto.mapper;

import com.makernav.categorize.dto.ItemDTO;
import com.makernav.categorize.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "idItem", target = "idItem")
    ItemDTO toDTO(Item item);

    @Mapping(target = "idItem", ignore = true)
    Item toEntity(ItemDTO itemDTO);

    @Mapping(target = "idItem", ignore = true)
    void updateEntityFromDTO(ItemDTO itemDTO, @MappingTarget Item item);
}
