package com.sol.snappick.product.service;

import com.sol.snappick.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;


}
