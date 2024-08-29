package com.sol.snappick.cart.service;

import com.sol.snappick.cart.dto.CartItemReq;
import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.exception.CartItemNotFoundException;
import com.sol.snappick.cart.exception.CartNotFoundException;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.exception.AccessDeniedException;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.product.dto.CartCreateRes;
import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartItem;
import com.sol.snappick.product.entity.CartStatus;
import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.exception.ProductNotFoundException;
import com.sol.snappick.product.exception.QuantityException;
import com.sol.snappick.product.repository.CartItemRepository;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.product.repository.ProductRepository;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.exception.StoreNotFoundException;
import com.sol.snappick.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sol.snappick.product.mapper.ProductMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    private final ProductMapper productMapper;

    @Transactional
    public CartCreateRes createCart(Integer memberId, Integer storeId) throws Exception {

        //유효성 검증
        //1) member가 존재하는지 확인한다.
        Member customer = memberRepository.findById(memberId)
                .orElseThrow(() -> new AccessDeniedException("존재하지 않는 회원입니다."));

        //2) store가 존재하는지 확인한다.
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        //3) CartStatus."결제대기" 중인 카트가 있는지 확인한다.
        // 없다면 카트를 생성한다.
        Cart cartToCreate = cartRepository.findByStoreIdAndCustomerIdAndStatus(storeId, memberId, CartStatus.결제대기);
        if (cartToCreate==null) {
            cartToCreate = Cart.builder()
                    .store(store)
                    .customer(customer)
                    .status(CartStatus.결제대기)
                    .items(new ArrayList<>())
                    .build();
        }
        //있다면 담겨있던 cartItem을 모두 삭제한다.
        else {
            cartItemRepository.deleteByCartId(cartToCreate.getId());
            cartToCreate.getItems().clear();
        }
        cartToCreate = cartRepository.save(cartToCreate);

        //Cart: store, customer, transaction, items, status
        //To
        //CartRes: id, store_id, customer_id, transaction_id, items.size(), status

        return CartCreateRes.builder()
                .cartId(cartToCreate.getId())
                .build();
    }

    @Transactional
    public CartItemRes createCartItem(Integer memberId, Integer cartId, CartItemReq cartItemReq) throws Exception {

        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
//        if (cart.getCustomer().getId()!=memberId)
//            throw new AccessDeniedException();

        //3) 구매하려는 product가 존재하는지 확인한다.
        Product product = productRepository.findById(cartItemReq.getProductId())
                .orElseThrow(()->new ProductNotFoundException());

        //4) 주문 수량이 재고의 개수보다 작은지 확인한다.
        if (cartItemReq.getQuantity()>product.getStock()){
            throw new QuantityException();
        }

        //5) 주문 수량이 인당 개수 제한을 만족하는지 확인한다.
        if (cartItemReq.getQuantity()>product.getPersonalLimit()){
            throw new QuantityException("인당 구매 가능 수량을 넘어서는 수량은 구매할 수 없습니다.");
        }

        //TODO 6) 주문 수량이 일일 개수 제한을 만족하는지 확인한다.

        //cart에 cartItem을 추가한다.
        CartItem cartItemToCreate = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(cartItemReq.getQuantity())
                .build();
        cartItemToCreate = cartItemRepository.save(cartItemToCreate);

        //CartItem: id, cart, product, quantity
        //To
        //CartItemRes: id, product, quantity

        return CartItemRes.builder()
                .id(cartItemToCreate.getId())
                .product(productMapper.toSimpleDto(product))
                .quantity(cartItemToCreate.getQuantity())
                .build();
    }

    @Transactional
    public CartItemRes updateCartItem(Integer memberId, Integer cartId, Integer itemId, CartItemReq cartItemReq) throws Exception {

        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
//        if (cart.getCustomer().getId()!=memberId)
//            throw new AccessDeniedException();

        //3) cartItem이 존재하는지 확인한다.
        CartItem cartItemToUpdate = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException());

        //4) 주문 수량이 재고의 개수를 넘지 않는지 확인한다.
        Product product = cartItemToUpdate.getProduct();
        if (cartItemReq.getQuantity()>product.getStock()){
            throw new QuantityException();
        }

        //5) 주문 수량이 인당 개수 제한을 만족하는지 확인한다.
        if (cartItemReq.getQuantity()>product.getPersonalLimit()){
            throw new QuantityException("인당 구매 가능 수량을 넘어서는 수량은 구매할 수 없습니다.");
        }

        //TODO 6) 주문 수량이 일일 개수 제한을 만족하는지 확인한다.

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

        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
//        if (cart.getCustomer().getId()!=memberId)
//            throw new AccessDeniedException();

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
    public Boolean deleteCartItem(Integer memberId, Integer cartId, Integer itemId) throws Exception {
        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
        if (cart.getCustomer().getId()!=memberId)
            throw new AccessDeniedException();

        //3) cartItem이 존재하는지 확인한다.
        CartItem cartItemToDelete = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException());

        //cartItem을 삭제한다.
        try {
            cartItemRepository.delete(cartItemToDelete);
            return true;
        } catch(Exception  e){
            return false;
        }
    }
}
