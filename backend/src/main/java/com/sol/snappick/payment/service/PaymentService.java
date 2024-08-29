package com.sol.snappick.payment.service;

import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.mapper.CartItemMapper;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.payment.dto.CustomerRes;
import com.sol.snappick.payment.dto.ReceiptRes;
import com.sol.snappick.payment.dto.StoreSimpleRes;
import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartItem;
import com.sol.snappick.product.entity.CartStatus;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.exception.StoreNotFoundException;
import com.sol.snappick.store.repository.StoreImageRepository;
import com.sol.snappick.store.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    public List<ReceiptRes> getPendingCustomers(Integer storeId) {
        //유효성 검증
        //1) 스토어가 존재하는지 확인한다.
        Store store = storeRepository.findById(storeId)
                                     .orElseThrow(() -> new StoreNotFoundException());

        //storeId를 갖는 모든 결제완료(=대기 수령) cart들을 조회한다.
        List<Cart> pendingCarts = cartRepository.findByStoreIdAndStatus(storeId, CartStatus.결제완료);

        List<ReceiptRes> pendingCustomers = new ArrayList<>();

        //store
        List<StoreImage> storeImages = storeImageRepository.findAllByStore(store);
        List<String> thumbnailImages = storeImages.stream()
                                                  .map(StoreImage::getThumbnailImageUrl)
                                                  .collect(Collectors.toList());
        StoreSimpleRes nowStore = StoreSimpleRes.builder()
                                                .id(store.getId())
                                                .name(store.getName())
                                                .location(store.getLocation())
                                                .images(thumbnailImages)
                                                .build();

        for (Cart cart : pendingCarts) {
            List<CartItem> items = cart.getItems();
            List<CartItemRes> cartItemResList = CartItemMapper.toCartItemResList(items);

            CustomerRes singleCustomer = CustomerRes.builder()
                                                    .memberId(cart.getCustomer()
                                                                  .getId())
                                                    .name(cart.getCustomer()
                                                              .getName())
                                                    .phoneNumber(cart.getCustomer()
                                                                     .getPhoneNumber())
                                                    .build();

            ReceiptRes receipt = ReceiptRes.builder()
                                           .id(cart.getId())
                                           .status(CartStatus.결제완료)
                                           .store(nowStore)
                                           .customer(singleCustomer)
                                           .totalPrice(cart.getTransaction()
                                                           .getVariation())
                                           .transactedAt(cart.getTransaction()
                                                             .getTransactedAt())
                                           .items(cartItemResList)
                                           .build();

            pendingCustomers.add(receipt);
        }

        return pendingCustomers;
    }

    //    public String attemptPayment(Integer memberId, Integer cartId) {
    //
    //        //구매자 ID
    //        Integer customerId = memberId;
    //
    //        Cart cart = cartRepository.findById(cartId)
    //                                  .orElseThrow(() -> new CartNotFoundException());
    //
    //        //판매자 ID
    //        Integer sellerId = cart.getStore().getMember().getId();
    //        Member seller = memberRepository.findById(sellerId)
    //                                        .orElseThrow(() -> new BasicNotFoundException());
    //
    //        //판매자 계좌번호
    //        String sellerAccountNumber = seller.getAccountNumber();
    //
    //        //총 결제 금액
    //        Integer sumPrice = 0;
    //        for (CartItem item: cart.getItems()){
    //            sumPrice += item.getQuantity() * item.getProduct().getPrice();
    //        }
    //
    //
    //
    //    }
}
