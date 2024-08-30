package com.sol.snappick.cart.controller;

import com.sol.snappick.cart.dto.CartItemReq;
import com.sol.snappick.cart.dto.CartItemRes;
import com.sol.snappick.cart.service.CartService;
import com.sol.snappick.product.dto.CartCreateRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "cart", description = "장바구니 : 장바구니 관리 API")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping()
    @Operation(summary = "카트 생성", description = "storeID를 입력해주세요. 해당 스토어의 상품을 담을 수 있는 카트를 생성합니다.")
    public ResponseEntity<CartCreateRes> createCart(
            Authentication authentication,
            @RequestBody Integer storeId
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());

        CartCreateRes response = cartService.createCart(memberId, storeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{cart_id}/items")
    @Operation(summary = "카트 상품 추가", description = """
            pathVariable로 cart_id를,
            body에는 productId, quantity를 담아서 보내주세요.
            """)
    public ResponseEntity<CartItemRes> createCartItem(
            Authentication authentication,
            @PathVariable("cart_id") Integer cartId,
            @RequestBody CartItemReq cartItemReq
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());

        CartItemRes response = cartService.createCartItem(memberId, cartId, cartItemReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cart_id}/items/{item_id}")
    @Operation(summary = "카트 상품 수정", description = """
            카트에 추가한 상품의 수량을 수정할 수 있습니다. \n
            PathVariable에 cart_id, item_id를 담아주시고, \n
            body에는 product_id, quantity를 담아서 보내주세요. \n
            """)
    public ResponseEntity<CartItemRes> updateCartItem(
            Authentication authentication,
            @PathVariable("cart_id") Integer cartId,
            @PathVariable("item_id") Integer itemId,
            @RequestBody CartItemReq cartItemReq
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());

        CartItemRes response = cartService.updateCartItem(memberId, cartId, itemId, cartItemReq);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{cart_id}/items")
    @Operation(summary = "카트 상품 조회", description = """
            카트에 추가한 상품들을 조회할 수 있습니다.\n
            pathVariable로 cart_id를 담아주세요.\n
            """)
    public ResponseEntity<List<CartItemRes>> updateCartItem(
            Authentication authentication,
            @PathVariable("cart_id") Integer cartId
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());
        List<CartItemRes> response = cartService.readCartItem(memberId, cartId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{cart_id}/items/{item_id}")
    @Operation(summary = "카트 상품 삭제", description = """
            cart_id, item_id를 이용해서 카트에 추가한 상품을 삭제할 수 있습니다.
            """)
    public ResponseEntity<Boolean> deleteCartItem(
            Authentication authentication,
            @PathVariable("cart_id") Integer cartId,
            @PathVariable("item_id") Integer itemId
    ) throws Exception {
        Integer memberId = Integer.valueOf(authentication.getName());

        Boolean response = cartService.deleteCartItem(memberId, cartId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
