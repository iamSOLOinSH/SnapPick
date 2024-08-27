package com.sol.snappick.store.mapper;

import com.sol.snappick.store.dto.StoreImageDto;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreImageMapper {

    default List<StoreImage> toEntityList(
        List<StoreImageDto> images,
        Store store
    ) {
        if (images == null || store == null) {
            return List.of(); // 빈 리스트를 반환하여 NPE 를 방지
        }
        return images.stream()
                     .map(dto -> StoreImage.builder()
                                           .thumbnailImageUrl(dto.getThumbnailImageUrl())
                                           .originImageUrl(dto.getOriginImageUrl())
                                           .store(store)
                                           .build())
                     .toList();
    }
}
