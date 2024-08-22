package com.sol.snappick.store.mapper;

import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreTag;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreTagMapper {

    default List<StoreTag> toEntityList(
        List<String> tags,
        Store store
    ) {
        if (tags == null || store == null) {
            return List.of(); // 빈 리스트를 반환하여 NPE 를 방지
        }
        return tags.stream()
                   .map(tag -> StoreTag.builder()
                                       .tag(tag)
                                       .store(store)
                                       .build())
                   .toList();
    }

}
