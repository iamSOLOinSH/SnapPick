package com.sol.snappick.product.repository;

import com.sol.snappick.product.entity.Cart;
import com.sol.snappick.product.entity.CartItem;
import com.sol.snappick.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCartId(Integer cartId);
    Boolean deleteByCartId(Integer cartId);
    CartItem findByIdAndProductId(Integer cartId, Integer productId);
    List<CartItem> findByProductId(Integer id);

    CartItem findByCartIdAndProductId(Integer id, Integer id1);
}
