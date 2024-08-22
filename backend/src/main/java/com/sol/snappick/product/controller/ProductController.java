package com.sol.snappick.product.controller;

import com.sol.snappick.product.dto.ProductCreateReq;
import com.sol.snappick.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/{store_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 등록", description = """
            상품을 multipart/form-data 형식으로 등록합니다 \n
             상품 이름(name), 상품 설명(description), 기본 가격(price), 총 수량(totalStock), 일일 판매 수량(dailyLimit), 1인 구매 가능 개수(personalLimit), \n
             상품 옵션 목록(선택사항)에는 옵션 별 이름(name), 추가 금액(plusPrice), 재고(stock)를 담아서 보내주세요. \n
             이미지 파일 목록(선택사항)을 보내주세요
            """)
    public ResponseEntity<Void> createPopupStore(
            @PathVariable(name = "store_id") Integer storeId,
            @RequestPart("productCreateReq") ProductCreateReq productCreateReq,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        // TODO memberId 추출

        // TODO 상품 생성 로직 구현

        return ResponseEntity.created(null).build();
    }
}