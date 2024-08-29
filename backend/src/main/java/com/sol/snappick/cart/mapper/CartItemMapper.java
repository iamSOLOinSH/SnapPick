package com.sol.snappick.cart.mapper;

import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.product.dto.ProductSimpleRes;
import com.sol.snappick.product.entity.CartItem;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class CartItemMapper {

    public static List<CartItemRes> toCartItemResList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItemMapper::toCartItemRes)
                .collect(Collectors.toList());
    }

    private static CartItemRes toCartItemRes(CartItem cartItem) {
        return CartItemRes.builder()
                .id(cartItem.getId())
                .product(toProductSimpleRes(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .build();
    }

    private static ProductSimpleRes toProductSimpleRes(Product product) {
        return ProductSimpleRes.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .thumbnailImageUrls(extractThumbnailUrls(product.getImages()))
                .build();
    }

    private static List<String> extractThumbnailUrls(List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getThumbnailImageUrl)
                .collect(Collectors.toList());
    }
}