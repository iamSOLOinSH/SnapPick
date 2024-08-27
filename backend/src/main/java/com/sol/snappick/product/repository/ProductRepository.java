package com.sol.snappick.product.repository;

import com.sol.snappick.product.entity.Product;
import com.sol.snappick.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public ArrayList<Product> findByStore(Store store);

}
