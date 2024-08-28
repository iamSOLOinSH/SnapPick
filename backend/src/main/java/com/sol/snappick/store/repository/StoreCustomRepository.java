package com.sol.snappick.store.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.entity.Store;

public interface StoreCustomRepository {

	List<Store> findBySellerId (Integer memberId);

	List<Store> findByConditions (
		StoreSearchReq searchReq,
		Pageable pageable
	);

	List<Store> findWithoutClosed();
}
