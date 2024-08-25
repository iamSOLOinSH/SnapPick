package com.sol.snappick.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreRunningTime;

public interface StoreRunningTimeRepository extends JpaRepository<StoreRunningTime, Long> {

	void deleteAllByStore (Store store);
}
