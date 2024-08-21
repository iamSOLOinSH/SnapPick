package com.sol.snappick.product.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.store.entity.Store;
import lombok.Getter;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    protected Product() {
    }
}
