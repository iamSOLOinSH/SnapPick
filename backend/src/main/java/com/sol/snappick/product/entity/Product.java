package com.sol.snappick.product.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.store.entity.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column
    private String description;

    @Column
    private Integer price;

    @Column
    private Integer dailyLimit;

    @Column
    private Integer personalLimit;

    // 상품 이미지
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @Setter
    private List<ProductImage> images;

    // 상품 옵션
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @Setter
    private List<ProductOption> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Setter
    private Store store;

    @Builder
    public Product(
            String name,
            String description,
            Integer price,
            Integer dailyLimit,
            Integer personalLimit,
            List<ProductImage> images,
            List<ProductOption> options,
            Store store) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.personalLimit = personalLimit;
        this.images = images;
        this.options = options;
        this.store = store;
    }

    public void updateDetails(
            String name,
            String description,
            Integer price,
            Integer dailyLimit,
            Integer personalLimit
            ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.personalLimit = personalLimit;
    }

}
