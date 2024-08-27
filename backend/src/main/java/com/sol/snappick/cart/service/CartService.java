package com.sol.snappick.cart.service;

import com.sol.snappick.cart.dto.CartItemReq;
import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.exception.CartNotFoundException;
import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartItem;
import com.sol.snappick.product.entity.ProductOption;
import com.sol.snappick.product.exception.ProductOptionNotFoundException;
import com.sol.snappick.product.repository.CartItemRepository;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.product.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductOptionRepository productOptionRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItemRes createCartItem(Integer cartId, CartItemReq cartItemReq) {

        //cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //option
        ProductOption productOption = productOptionRepository.findById(cartItemReq.getProductOptionId())
                .orElseThrow(()->new ProductOptionNotFoundException());

        //cart에 cartItem을 추가한다.
        CartItem cartItemToCreate = CartItem.builder()
                .cart(cart)
                .productOption(productOption)
                .quantity(cartItemReq.getQuantity())
                .build();
        cartItemToCreate = cartItemRepository.save(cartItemToCreate);

        return CartItemRes.builder()
                .id(cartItemToCreate.getId())
                .cartId(cartItemToCreate.getCart().getId())
                .productOptionId(cartItemToCreate.getProductOption().getId())
                .quantity(cartItemToCreate.getQuantity())
                .build();
    }
}
