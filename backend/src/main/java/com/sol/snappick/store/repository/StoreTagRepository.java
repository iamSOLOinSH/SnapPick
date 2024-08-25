package com.sol.snappick.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreTag;

public interface StoreTagRepository extends JpaRepository<StoreTag, Integer> {

	void deleteAllByStore (Store store);
}
