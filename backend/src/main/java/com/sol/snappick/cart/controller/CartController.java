package com.sol.snappick.cart.controller;

import com.sol.snappick.cart.dto.CartItemReq;
import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/{cart_id}/items")
    @Operation(summary = "카트 상품 추가", description = """
            productId, quantity를 담아서 보내주세요.
            """)
    public ResponseEntity<CartItemRes> createCartItem(
            @PathVariable("cart_id") Integer cartId,
            @RequestBody CartItemReq cartItemReq
    ) throws Exception{
        CartItemRes response = cartService.createCartItem(cartId, cartItemReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cart_id}/items/{item_id}")
    @Operation(summary = "카트 상품 수정", description = """
            카트에 추가한 상품의 옵션, 수량을 수정할 수 있습니다.
            """)
    public ResponseEntity<CartItemRes> updateCartItem(
            @PathVariable("cart_id") Integer cartId,
            @PathVariable("item_id") Integer itemId,
            @RequestBody CartItemReq cartItemReq
    ) throws Exception{
        CartItemRes response = cartService.updateCartItem(cartId, itemId, cartItemReq);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{cart_id}/items")
    @Operation(summary = "카트 상품 조회", description = """
            카트에 추가한 상품들을 조회할 수 있습니다.
            """)
    public ResponseEntity<List<CartItemRes>> updateCartItem(
            @PathVariable("cart_id") Integer cartId
    ) throws Exception{
        List<CartItemRes> response = cartService.readCartItem(cartId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{cart_id}/items/{item_id}")
    @Operation(summary = "카트 상품 삭데", description = """
            cart_id, item_id를 이용해서 카트에 추가한 상품을 삭제할 수 있습니다.
            """)
    public ResponseEntity<Boolean> deleteCartItem(
            @PathVariable("cart_id") Integer cartId,
            @PathVariable("item_id") Integer itemId
    ) throws Exception{
        Boolean response = cartService.deleteCartItem(cartId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
