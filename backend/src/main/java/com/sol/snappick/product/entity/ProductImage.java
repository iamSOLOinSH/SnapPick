package com.sol.snappick.product.entity;

import lombok.Getter;

import javax.persistence.*;

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
