package com.sol.snappick.product.mapper;

import com.sol.snappick.product.dto.*;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "store", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductCreateReq productCreateReq);

    @Mapping(target = "originImageUrls", source = "images", qualifiedByName = "toOriginImageUrls")
    ProductDetailRes toDetailDto(Product product);

    @Mapping(target = "thumbnailImageUrls", source = "images", qualifiedByName = "toThumbnailUrls")
    ProductSimpleRes toSimpleDto(Product product);

    @Named("toOriginImageUrls")
    default List<String> toOriginImageUrls(List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getOriginImageUrl).collect(Collectors.toList());
    }

    @Named("toThumbnailUrls")
    default List<String> toThumbnailUrls(List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getThumbnailImageUrl).collect(Collectors.toList());
    }
}
