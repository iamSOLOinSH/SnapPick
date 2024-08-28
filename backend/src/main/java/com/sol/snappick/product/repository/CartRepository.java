package com.sol.snappick.product.repository;

import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
