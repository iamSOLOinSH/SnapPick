package com.sol.snappick.product.repository;

import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByStoreIdAndCustomerIdAndStatus(Integer storeId, Integer memberId, CartStatus status);
    public List<Cart> findByStoreIdAndStatus(Integer storeId, CartStatus status);
    public List<Cart> findByStoreId(Integer storeId);
}
