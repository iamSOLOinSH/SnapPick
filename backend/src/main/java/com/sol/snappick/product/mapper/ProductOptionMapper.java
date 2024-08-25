package com.sol.snappick.product.mapper;

import com.sol.snappick.product.dto.ProductOptionReq;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductOption;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductOptionMapper {

    default List<ProductOption> toEntityList(
            List<ProductOptionReq> productOptionReqs,
            Product product
    ) {

        if (productOptionReqs==null || product==null){
            return List.of();
        }

        return productOptionReqs.stream()
                .map(req -> ProductOption.builder()
                                            .name(req.getName())
                        .plusPrice(req.getPlusPrice())
                        .stock(req.getStock())
                        .build())
                .toList();
    }

}
