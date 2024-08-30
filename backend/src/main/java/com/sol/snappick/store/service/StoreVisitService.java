package com.sol.snappick.store.service;

import com.sol.snappick.cart.service.CartService;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.service.BasicMemberService;
import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreVisit;
import com.sol.snappick.store.repository.StoreVisitRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreVisitService {

    private final StoreService storeService;

    private final StoreVisitRepository storeVisitRepository;
    private final CartService cartService;
    private final BasicMemberService basicMemberService;

    /**
     * 방문처리
     *
     * @param storeId  스토어 ID
     * @param memberId 사용자 ID
     */
    public void recordVisit(
        Integer storeId,
        Integer memberId
    ) throws Exception {
        // 스토어와 사용자를 데이터베이스에서 조회
        Store store = storeService.findStoreWithException(storeId);
        Member customer = basicMemberService.getMemberById(memberId);

        // 카트 조회 또는 생성
        Cart cart = cartService.getOrCreateCart(customer.getId(), customer, store);

        // 현재 시간을 방문 시간으로 설정
        LocalDateTime visitedAt = LocalDateTime.now();

        // 방문 기록 생성
        StoreVisit storeVisit = StoreVisit.builder()
                                          .visitedAt(visitedAt)
                                          .customer(customer)
                                          .store(store)
                                          .cart(cart)
                                          .build();

        // 방문 기록을 데이터베이스에 저장
        storeVisitRepository.save(storeVisit);
    }
}
