package com.sol.snappick.product.entity;

import com.sol.snappick.store.entity.Store;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column
    private Integer plusPrice;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    protected ProductOption() {
    }
}
