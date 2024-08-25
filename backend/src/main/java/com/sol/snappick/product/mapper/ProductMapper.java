package com.sol.snappick.product.mapper;

import com.sol.snappick.product.dto.*;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import com.sol.snappick.product.entity.ProductOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "store", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductCreateReq productCreateReq);

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "totalStock", source = "options", qualifiedByName = "calcTotalStock")
    @Mapping(target = "options", source = "options", qualifiedByName = "toOptionReqList")
    @Mapping(target = "originImageUrls", source = "images", qualifiedByName = "toOriginImageUrls")
    ProductDetailRes toDetailDto(Product product);

    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "totalStock", source = "options", qualifiedByName = "calcTotalStock")
    @Mapping(target = "thumbnailImageUrls", source = "images", qualifiedByName = "toThumbnailUrls")
    ProductSimpleRes toSimpleDto(Product product);

    @Named("calcTotalStock")
    default Integer calcTotalStock(List<ProductOption> productOptions) {
        return productOptions.stream()
                .mapToInt(ProductOption::getStock)
                .sum();
    }

    @Named("toOptionReqList")
    default List<ProductOptionReq> toOptionReqList(List<ProductOption> productOptions) {
        return productOptions.stream()
                .map(option->ProductOptionReq.builder()
                        .name(option.getName())
                        .plusPrice(option.getPlusPrice())
                        .stock(option.getStock())
                        .build())
                .toList();
    }

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
