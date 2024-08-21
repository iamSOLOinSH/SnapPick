package com.sol.snappick.store.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

//    @Mapping(target = "tags", ignore = true)
//    Store toEntity(StoreCreateReq dto);
//
//    @Mapping(source = "entity.tags", target = "tags")
//    StoreRes toDto(Store entity);
//
//    default List<String> mapTags(List<StoreTag> tags) {
//        return tags.stream()
//                   .map(StoreTag::getTag)
//                   .collect(Collectors.toList());
//    }
//    default List<StoreTag> mapTagStringsToEntities(List<String> tags, @MappingTarget Store store) {
//        return tags.stream().map(tag -> StoreTag.builder())
//    }
}
