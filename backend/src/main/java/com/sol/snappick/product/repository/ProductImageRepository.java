package com.sol.snappick.product.repository;

import com.sol.snappick.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<Product, Integer> {
}
