package com.sol.snappick.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String originImageUrl;

    @Column
    private String thumbnailImageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected ProductImage() {
    }
}
