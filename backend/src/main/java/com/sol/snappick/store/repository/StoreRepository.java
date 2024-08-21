package com.sol.snappick.store.repository;

import com.sol.snappick.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {

}
