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
import lombok.Getter;

@Entity
@Getter
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
    private List<ProductImage> images;

    // 상품 옵션
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductOption> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    protected Product() {
    }
}
