package com.sol.snappick.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column (columnDefinition = "TEXT")
	private String originImageUrl;

	@Column (columnDefinition = "TEXT")
	private String thumbnailImageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductImage(
            Integer id,
            String originImageUrl,
            String thumbnailImageUrl,
            Product product) {
        this.id = id;
        this.originImageUrl = originImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.product = product;
    }
}
