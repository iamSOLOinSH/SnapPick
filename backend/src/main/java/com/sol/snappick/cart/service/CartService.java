package com.sol.snappick.cart.service;

import com.sol.snappick.cart.dto.CartItemReq;
import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.exception.CartItemNotFoundException;
import com.sol.snappick.cart.exception.CartNotFoundException;
import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartItem;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.exception.ProductNotFoundException;
import com.sol.snappick.product.exception.QuantityOverProductStockException;
import com.sol.snappick.product.repository.CartItemRepository;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sol.snappick.product.mapper.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    private final ProductMapper productMapper;

    @Transactional
    public CartItemRes createCartItem(Integer cartId, CartItemReq cartItemReq) {

        //cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //product가 존재하는지 확인한다.
        Product product = productRepository.findById(cartItemReq.getProductId())
                .orElseThrow(()->new ProductNotFoundException());

        //주문 수량이 재고의 개수를 넘지 않는지 확인한다.
        if (cartItemReq.getQuantity()>product.getStock()){
            throw new QuantityOverProductStockException();
        }

        //cart에 cartItem을 추가한다.
        CartItem cartItemToCreate = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(cartItemReq.getQuantity())
                .build();
        cartItemToCreate = cartItemRepository.save(cartItemToCreate);

        return CartItemRes.builder()
                .id(cartItemToCreate.getId())
                .product(productMapper.toSimpleDto(product))
                .quantity(cartItemToCreate.getQuantity())
                .build();
    }

    @Transactional
    public CartItemRes updateCartItem(Integer cartId, Integer itemId, CartItemReq cartItemReq) {

        //cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //cartItem가 존재하는지 확인한다.
        CartItem cartItemToUpdate = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException());

        //quantity를 갱신한다.
        cartItemToUpdate.updateDetails(cartItemReq.getQuantity());
        cartItemToUpdate = cartItemRepository.save(cartItemToUpdate);
        return CartItemRes.builder()
                .id(cartItemToUpdate.getId())
                .product(productMapper.toSimpleDto(cartItemToUpdate.getProduct()))
                .quantity(cartItemToUpdate.getQuantity())
                .build();
    }

    @Transactional
    public List<CartItemRes> readCartItem(Integer cartId) {

        //cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //cart에 속하는 cartItem을 조회한다.
        List<CartItem> items = cartItemRepository.findByCart(cart);
        return items.stream().map(item -> (
                CartItemRes.builder()
                        .id(item.getId())
                        .product(productMapper.toSimpleDto(item.getProduct()))
                        .quantity(item.getQuantity())
                        .build()
                )).collect(Collectors.toList());
    }

    @Transactional
    public Boolean deleteCartItem(Integer cartId, Integer itemId) {
        //cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //cartItem이 존재하는지 확인한다.
        CartItem cartItemToDelete = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException());

        //cartItem 삭제 시도한다.
        try {
            cartItemRepository.delete(cartItemToDelete);
            return true;
        } catch(Exception  e){
            return false;
        }
    }
}
