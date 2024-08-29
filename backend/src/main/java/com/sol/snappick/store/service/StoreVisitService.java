package com.sol.snappick.store.service;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.service.BasicMemberService;
import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartStatus;
import com.sol.snappick.product.repository.CartRepository;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreVisit;
import com.sol.snappick.store.repository.StoreVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class StoreVisitService {
    private final StoreService storeService;

    private final StoreVisitRepository storeVisitRepository;
    private final BasicMemberService basicMemberService;
    private final CartRepository cartRepository;

    /**
     * 방문처리
     *
     * @param storeId  스토어 ID
     * @param memberId 사용자 ID
     * @param isVisit  방문 여부 (방문 기록을 남길지 여부)
     */
    public void recordVisit(
            Integer storeId,
            Integer memberId,
            boolean isVisit
    ) {
        if (!isVisit) {
            // 방문 기록을 남기지 않는 경우, 메서드를 바로 종료
            return;
        }

        // 스토어와 사용자를 데이터베이스에서 조회
        Store store = storeService.findStoreWithException(storeId);
        Member member = basicMemberService.getMemberById(memberId);

        // TODO : 이 로직이 합리적인지 물어볼것. (방문 처리->카트 생성)
        // TODO : 구매자가 방문할 때마다 visit 처리하는게 맞는지 물어볼것
        // TODO : 같은 말이긴한데 isPaid가 true일 때만 새 카트를 만드는게 맞을지 물어볼것
        // 방문 처리 -> 새 카트 생성
        Cart newCart = Cart.builder()
                .store(store)
                .customer(member)
                .status(CartStatus.결제대기)
                .build();
        cartRepository.save(newCart);

        // 현재 시간을 방문 시간으로 설정
        LocalDateTime visitedAt = LocalDateTime.now();

        // 방문 기록 생성
        StoreVisit storeVisit = StoreVisit.builder()
                .visitedAt(visitedAt)
                .customer(member)
                .store(store)
                .cart(newCart)
                .build();

        // 방문 기록을 데이터베이스에 저장
        storeVisitRepository.save(storeVisit);
    }
}
