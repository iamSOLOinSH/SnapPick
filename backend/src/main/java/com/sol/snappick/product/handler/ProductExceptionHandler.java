package com.sol.snappick.product.handler;

import com.sol.snappick.product.exception.ProductBadRequestException;
import com.sol.snappick.product.exception.ProductImageLimitExceedException;
import com.sol.snappick.product.exception.ProductNotFoundException;
import com.sol.snappick.product.exception.QuantityOverProductStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(e.getMessage());
    }

    @ExceptionHandler(ProductImageLimitExceedException.class)
    public ResponseEntity<String> handleProductImageLimitExceedException(ProductImageLimitExceedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    @ExceptionHandler(ProductBadRequestException.class)
    public ResponseEntity<String> handleProductBadRequestException(ProductBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    @ExceptionHandler(QuantityOverProductStockException.class)
    public ResponseEntity<String> handleQuantityOverProductStockException(QuantityOverProductStockException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
