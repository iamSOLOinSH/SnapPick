package com.sol.snappick.cart.controller;

import com.sol.snappick.cart.dto.CartItemReq;
import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/{cart_id}")
    @Operation(summary = "카트 상품 추가", description = """
            productOptionId, quantity를 담아서 보내주세요.
            """)
    public ResponseEntity<CartItemRes> createCartItem(
            @PathVariable("cart_id") Integer cartId,
            @RequestBody CartItemReq cartItemReq
    ) throws Exception{
        CartItemRes response = cartService.createCartItem(cartId, cartItemReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
