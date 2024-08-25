package com.sol.snappick.product.mapper;

import com.sol.snappick.product.dto.ProductImageDto;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import com.sol.snappick.store.entity.StoreTag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    default List<ProductImage> toEntityList(
            List<ProductImageDto> images,
            Product product
    ) {
        if (images == null || product == null) {
            return List.of();
        }
        return images.stream()
                .map(image -> ProductImage.builder()
                        .originImageUrl(image.getOriginImageUrl())
                        .thumbnailImageUrl(image.getThumbnailImageUrl())
                        .product(product)
                        .build())
                .toList();
    }

}
