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
import com.sol.snappick.product.mapper.ProductMapper;
import com.sol.snappick.product.repository.CartItemRepository;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.product.repository.ProductRepository;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.exception.StoreNotFoundException;
import com.sol.snappick.store.repository.StoreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CartCreateRes createCart(
            Integer memberId,
            Integer storeId
    ) throws Exception {

        //유효성 검증
        // - member가 존재하는지 확인한다.
        Member customer = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new AccessDeniedException("존재하지 않는 회원입니다."));
        // - store가 존재하는지 확인한다.
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());
        // - store의 판매자가 이 사용자가 맞는지 확인한다.
        if (memberId.equals(store.getMember().getId()))
            throw new AccessDeniedException();

        // - 결제대기 중인 카트가 이미 존재하는지 확인한다.
        Cart cartToCreate = cartRepository.findByStoreIdAndCustomerIdAndStatus(
                storeId, memberId, CartStatus.결제대기);
        // 없다면, 카트를 생성한다.
        if (cartToCreate == null) {
            cartToCreate = Cart.builder()
                    .store(store)
                    .customer(customer)
                    .status(CartStatus.결제대기)
                    .items(new ArrayList<>())
                    .build();
        }
        //있다면, 담겨 있던 cartItem을 초기화한다.
        else {
            cartItemRepository.deleteByCartId(cartToCreate.getId());
            cartToCreate.getItems()
                    .clear();
        }

        // DB에 생성한 카트를 저장한다.
        cartToCreate = cartRepository.save(cartToCreate);
        return CartCreateRes.builder()
                .cartId(cartToCreate.getId())
                .build();
    }

    @Transactional
    public CartItemRes createCartItem(
            Integer memberId,
            Integer cartId,
            CartItemReq cartItemReq
    ) throws Exception {

        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
        if (!Objects.equals(cart.getCustomer().getId(), memberId))
            throw new AccessDeniedException();

        //3) 구매하려는 product가 존재하는지 확인한다.
        Product product = productRepository.findById(cartItemReq.getProductId())
                .orElseThrow(() -> new ProductNotFoundException());

        //4) 장바구니에 동일 상품이 있는지 확인한다.
        CartItem cartItemToCreate = cartItemRepository.findByIdAndProductId(
                cartId, product.getId());
        //없으면 새로 생성한다.
        if (cartItemToCreate == null) {
            cartItemToCreate = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemReq.getQuantity())
                    .build();
        }
        //이미 존재한다면 반영해서 quantity를 수정한다.
        else {
            cartItemToCreate.setQuantity(
                    cartItemReq.getQuantity() + cartItemToCreate.getQuantity());
        }

        //5) 주문 수량이 재고의 개수보다 적은지 확인한다.
        if (cartItemToCreate.getQuantity() > product.getStock()) {
            throw new QuantityException();
        }

        //6) 주문 수량이 인당 개수 제한을 만족하는지 확인한다.
        if (product.getPersonalLimit() != 0 && cartItemToCreate.getQuantity() > product.getPersonalLimit()) {
            throw new QuantityException("인당 구매 수량 제한이 초과되었습니다.");
        }

        //7) 누적 주문 수량이 일일 개수 제한을 만족하는지 확인한다.
        List<Cart> processedCarts = cartRepository.findByStoreId(cart.getStore().getId());
        LocalDate today = LocalDate.now();
        List<Integer> filteredCartIds = processedCarts.stream()
                .filter(filteredCart -> (filteredCart.getStatus() == CartStatus.결제완료 || filteredCart.getStatus() == CartStatus.수령완료))
                .filter(filteredCart -> (filteredCart.getTransaction() != null
                        && filteredCart.getTransaction().getTransactedAt() != null
                        && filteredCart.getTransaction().getTransactedAt().toLocalDate().isEqual(today)))
                .map(Cart::getId)
                .toList();
        Integer totalQuantity = 0;
        for (Integer filteredCartId : filteredCartIds) {
            CartItem item = cartItemRepository.findByCartIdAndProductId(filteredCartId, product.getId());
            if (item != null) totalQuantity += item.getQuantity();
        }
        if (totalQuantity + cartItemReq.getQuantity() > product.getDailyLimit()) {
            throw new QuantityException("일일 수량 제한이 초과되었습니다.");
        }

        //cart에 cartItem을 추가한다.
        cartItemToCreate = cartItemRepository.save(cartItemToCreate);
        return CartItemRes.builder()
                .id(cartItemToCreate.getId())
                .product(productMapper.toSimpleDto(product))
                .quantity(cartItemToCreate.getQuantity())
                .build();
    }

    @Transactional
    public CartItemRes updateCartItem(
            Integer memberId,
            Integer cartId,
            Integer itemId,
            CartItemReq cartItemReq
    ) throws Exception {

        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
        if (cart.getCustomer().getId() != memberId)
            throw new AccessDeniedException();

        //3) cartItem이 존재하는지 확인한다.
        CartItem cartItemToUpdate = cartItemRepository.findById(itemId)
                .orElseThrow(
                        () -> new CartItemNotFoundException());

        //4) 주문 수량이 재고의 개수를 넘지 않는지 확인한다.
        Product product = cartItemToUpdate.getProduct();
        if (cartItemReq.getQuantity() > product.getStock()) {
            throw new QuantityException();
        }

        //5) 주문 수량이 인당 개수 제한을 만족하는지 확인한다.
        if (product.getPersonalLimit() != 0 && cartItemReq.getQuantity() > product.getPersonalLimit()) {
            throw new QuantityException("인당 구매 가능 수량을 넘어서는 수량은 구매할 수 없습니다.");
        }

        //6) 주문 수량이 일일 개수 제한을 만족하는지 확인한다.
        List<Cart> processedCarts = cartRepository.findByStoreId(cart.getStore().getId());
        LocalDate today = LocalDate.now();
        List<Integer> filteredCartIds = processedCarts.stream()
                .filter(filteredCart -> (filteredCart.getStatus() == CartStatus.결제완료 || filteredCart.getStatus() == CartStatus.수령완료))
                .filter(filteredCart -> (filteredCart.getTransaction() != null
                        && filteredCart.getTransaction().getTransactedAt() != null
                        && filteredCart.getTransaction().getTransactedAt().toLocalDate().isEqual(today)))
                .map(Cart::getId)
                .toList();
        Integer totalQuantity = 0;
        for (Integer filteredCartId : filteredCartIds) {
            CartItem item = cartItemRepository.findByCartIdAndProductId(filteredCartId, product.getId());
            if (item != null) totalQuantity += item.getQuantity();
        }
        if (totalQuantity + cartItemReq.getQuantity() > product.getDailyLimit()) {
            throw new QuantityException("일일 수량 제한이 초과되었습니다.");
        }

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
    public List<CartItemRes> readCartItem(Integer memberId, Integer cartId) throws Exception {

        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
        if (!Objects.equals(cart.getCustomer().getId(), memberId))
            throw new AccessDeniedException();

        //cart에 속하는 cartItem을 조회한다.
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        return items.stream()
                .map(item -> (CartItemRes.builder()
                        .id(item.getId())
                        .product(productMapper.toSimpleDto(item.getProduct()))
                        .quantity(item.getQuantity())
                        .build()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean deleteCartItem(
            Integer memberId,
            Integer cartId,
            Integer itemId
    ) throws Exception {
        //유효성 검증
        //1) cart가 존재하는지 확인한다.
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        //2) cart 접근 권한을 확인한다.
        if (cart.getCustomer()
                .getId() != memberId) {
            throw new AccessDeniedException();
        }

        //3) cartItem이 존재하는지 확인한다.
        CartItem cartItemToDelete = cartItemRepository.findById(itemId)
                .orElseThrow(
                        () -> new CartItemNotFoundException());

        //cartItem을 삭제한다.
        try {
            cartItemRepository.delete(cartItemToDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 결제안한 카트 또는 없으면 생성해서 반환
     *
     * @param customer
     * @param store
     * @return
     * @throws Exception
     */
    @Transactional
    public Cart getOrCreateCart(
            Integer memberId,
            Member customer,
            Store store
    ) throws Exception {
        //1) CartStatus."결제대기" 중인 카트가 있는지 확인한다.
        Cart cartToCreate = cartRepository.findByStoreIdAndCustomerIdAndStatus(
                store.getId(), customer.getId(), CartStatus.결제대기);
        if (cartToCreate == null) {
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
            cartToCreate.getItems()
                    .clear();
        }
        cartToCreate = cartRepository.save(cartToCreate);

        return cartToCreate;
    }

    @Transactional(readOnly = true)
    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
    }
}
