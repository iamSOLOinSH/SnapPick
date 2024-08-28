package com.sol.snappick.product.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.store.entity.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
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
    private Integer stock;

    @Column
    private Integer dailyLimit;

    @Column
    private Integer personalLimit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @Setter
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column
    @Setter
    private ProductStatus status;

    @Builder
    public Product(
            String name,
            String description,
            Integer price,
            Integer stock,
            Integer dailyLimit,
            Integer personalLimit,
            List<ProductImage> images,
            Store store,
            ProductStatus status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock=stock;
        this.dailyLimit = dailyLimit;
        this.personalLimit = personalLimit;
        this.images = new ArrayList<>();
        this.store = store;
        this.status = status;
    }

    public void updateDetails(
            String name,
            String description,
            Integer price,
            Integer stock,
            Integer dailyLimit,
            Integer personalLimit
            ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock=stock;
        this.dailyLimit = dailyLimit;
        this.personalLimit = personalLimit;
    }

    // 이미지 관련 메서드
    public void setImages(List<ProductImage> newImages) {
        this.images.clear();
        if (newImages != null) {
            for (ProductImage image : newImages) {
                addImage(image);
            }
        }
    }

    private void addImage(ProductImage image) {
        this.images.add(image);
        image.setProduct(this);
    }

}
