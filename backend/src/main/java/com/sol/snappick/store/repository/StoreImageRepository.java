package com.sol.snappick.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;

public interface StoreImageRepository extends JpaRepository<StoreImage, Integer> {

	void deleteAllByStore (Store store);
}
