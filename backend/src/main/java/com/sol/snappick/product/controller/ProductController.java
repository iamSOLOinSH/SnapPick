package com.sol.snappick.product.controller;

import com.sol.snappick.product.dto.ProductCreateReq;
import com.sol.snappick.product.dto.ProductDetailRes;
import com.sol.snappick.product.dto.ProductSimpleRes;
import com.sol.snappick.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 등록", description = """
            상품을 multipart/form-data 형식으로 등록합니다. \n
             상품 이름(name), 상품 설명(description), 기본 가격(price), 총 수량(totalStock), 일일 판매 수량(dailyLimit), 1인 구매 가능 개수(personalLimit), \n
             상품 옵션 목록(선택사항)에는 옵션 별 이름(name), 추가 금액(plusPrice), 재고(stock)를 담아서 보내주세요. \n
             이미지 파일 목록(선택사항)을 보내주세요.
            """)
    public ResponseEntity<ProductDetailRes> createProduct(
            @RequestParam(name = "store_id") Integer storeId,
            @RequestPart("productCreateReq") ProductCreateReq productCreateReq,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws Exception{
        // TODO memberId 추출

        // 상품 생성 로직
        ProductDetailRes response = productService.createProduct(storeId, productCreateReq, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping(value = "/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 수정", description = """
            기존에 등록한 상품을 수정합니다. \n
            """)
    public ResponseEntity<ProductDetailRes> updateProduct(
            @PathVariable(name = "product_id") Integer productId,
            @RequestPart("productCreateReq") ProductCreateReq productCreateReq,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws Exception {
        // TODO 유효성 검사
        // 1) 처리할 상품이 있는지 확인한다.
//        if (!productService.isProductExist(productId)){
//            return ResponseEntity.notFound().build();
//        }

        // 상품 수정 로직
        ProductDetailRes response = productService.updateProduct(productId, productCreateReq, images);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping(value = "/{product_id}")
    @Operation(summary = "상품 삭제", description = """
            기존에 등록한 상품을 삭제합니다. \n
            """)
    public ResponseEntity<Boolean> deleteProduct(
            @PathVariable(name = "product_id") Integer productId
    ) throws Exception {
        // TODO 유효성 검사
        // 1) 처리할 상품이 있는지 확인한다.
//        if (!productService.isProductExist(productId)){
//            return ResponseEntity.notFound().build();
//        }

        // 상품 삭제 로직
        boolean isDeleted = productService.deleteProduct(productId);
        return ResponseEntity.ok(isDeleted);
    }


    @GetMapping
    @Operation(summary = "상품 목록 조회", description = """
            스토어의 모든 상품을 조회합니다. \n
            """)
    public ResponseEntity<List<ProductSimpleRes>> getProducts(
            @RequestParam(name = "store_id") Integer storeId
    ) throws Exception {
        // TODO 유효성 검사
        // 1) 처리할 팝업스토어가 있는지 확인한다.
//        if (!storeService.isStoreExist(storeId)){
//            return ResponseEntity.notFound().build();
//        }

        // 상품 조회 로직
        List<ProductSimpleRes> response = productService.readStoreProducts(storeId);
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/{product_id}")
    @Operation(summary = "상품 상세 조회", description = """
            단일 상품의 상세 정보를 조회합니다. \n
            """)
    public ResponseEntity<ProductDetailRes> getProduct(
            @PathVariable(name = "product_id") Integer productId
    ) throws Exception {
        // TODO 유효성 검사
        // 1) 처리할 상품이 있는지 확인한다.
//        if (!productService.isProductExist(productId)){
//            return ResponseEntity.notFound().build();
//        }

        // TODO 상품 조회 로직
        ProductDetailRes response = productService.readProduct(productId);
        return ResponseEntity.ok(response);
    }

}