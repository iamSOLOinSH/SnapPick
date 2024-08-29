package com.sol.snappick.store.repository;

import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.VisitedStoreRes;
import com.sol.snappick.store.entity.Store;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface StoreCustomRepository {

    List<Store> findBySellerId(Integer memberId);

    List<Store> findByConditions(
        StoreSearchReq searchReq,
        Pageable pageable
    );

    List<Store> findWithoutClosed();

    List<VisitedStoreRes> findVisitedStoresByMember(Integer memberId);
}
