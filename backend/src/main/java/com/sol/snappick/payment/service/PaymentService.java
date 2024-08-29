package com.sol.snappick.payment.service;

import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.exception.CartNotFoundException;
import com.sol.snappick.cart.exception.CartUnauthorizedException;
import com.sol.snappick.cart.mapper.CartItemMapper;
import com.sol.snappick.cart.service.CartService;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Transaction;
import com.sol.snappick.member.exception.BasicNotFoundException;
import com.sol.snappick.member.repository.MemberRepository;
import com.sol.snappick.member.repository.TransactionRepository;
import com.sol.snappick.member.service.BasicMemberService;
import com.sol.snappick.member.service.TransactionService;
import com.sol.snappick.payment.dto.CustomerRes;
import com.sol.snappick.payment.dto.ReceiptRes;
import com.sol.snappick.payment.dto.StoreSimpleRes;
import com.sol.snappick.product.entity.*;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.product.repository.ProductRepository;
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
    private final BasicMemberService basicMemberService;
    private final CartService cartService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    public Boolean cartItemReceived(Integer cartId){

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new CartNotFoundException());

        //카트가 결제완료 상태인지 확인한다.
        if (cart.getStatus()==CartStatus.결제완료){
            cart.setStatus(CartStatus.수령완료);
            cartRepository.save(cart);
            return true;
        }
        return false;

    }

    public Boolean attemptTransfer(Integer customerId, Integer cartId) {
        Member customer = memberRepository.findById(customerId)
                .orElseThrow(() -> new BasicNotFoundException("해당하는 사용자를 찾을 수 없습니다."));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException());

        Member seller = cart.getStore().getMember();

        long totalAmount = cart.calculateTotalAmount();

        Integer transactionId = transactionService.transfer(customer, seller, customer.getAccountNumber(), totalAmount);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BasicNotFoundException("해당하는 결제 내역을 찾을 수 없습니다."));

        cart.setTransaction(transaction);
        cart.markAsPaid();

        updateProductStock(cart);

        cartRepository.save(cart);

        return true;
    }

    private void updateProductStock(Cart cart) {
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.decreaseStock(item.getQuantity());
        }
    }


    public List<ReceiptRes> getPendingCustomers(Integer memberId, Integer storeId) {
        //유효성 검증
        //0) 회원이 존재하는지 확인한다.
        boolean memeberIsExist = memberRepository.existsById(memberId);
        if (!memeberIsExist) throw new BasicNotFoundException();

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

    public ReceiptRes getReceipt(
        Integer cartId,
        Integer memberId
    ) {
        // 카트와 회원 정보를 조회
        Cart cart = cartService.getCartById(cartId);
        Member member = basicMemberService.getMemberById(memberId);

        // 카트가 해당 회원의 것인지 확인
        if (!cart.getCustomer()
                 .getId()
                 .equals(memberId)) {
            throw new CartUnauthorizedException();
        }

        // 카트 아이템 목록 조회
        List<CartItem> items = cart.getItems();
        List<CartItemRes> cartItemResList = CartItemMapper.toCartItemResList(items);

        Store store = cart.getStore();
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

        CustomerRes customer = CustomerRes.builder()
                                          .memberId(cart.getCustomer()
                                                        .getId())
                                          .name(cart.getCustomer()
                                                    .getName())
                                          .phoneNumber(cart.getCustomer()
                                                           .getPhoneNumber())
                                          .build();

        // 영수증 정보 생성
        ReceiptRes receipt = ReceiptRes.builder()
                                       .id(cart.getId())
                                       .status(cart.getStatus())
                                       .store(nowStore)
                                       .customer(customer)
                                       .totalPrice(cart.getTransaction()
                                                       .getVariation())
                                       .transactedAt(cart.getTransaction()
                                                         .getTransactedAt())
                                       .items(cartItemResList)
                                       .build();

        return receipt;
    }


}
