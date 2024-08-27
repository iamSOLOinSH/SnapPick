package com.sol.snappick.product.repository;

import com.sol.snappick.product.entity.Product;
import com.sol.snappick.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {



}
