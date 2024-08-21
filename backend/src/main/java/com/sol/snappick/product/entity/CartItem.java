package com.sol.snappick.product.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productoption_id")
    private ProductOption productOption;

    @Column
    private Integer quantity;

    protected CartItem() {
    }
}
