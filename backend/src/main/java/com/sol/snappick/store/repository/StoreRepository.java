package com.sol.snappick.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sol.snappick.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer>, StoreCustomRepository {

}
