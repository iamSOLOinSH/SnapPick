package com.sol.snappick.product.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Transaction;
import com.sol.snappick.store.entity.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member customer;

    @Enumerated(EnumType.STRING)
    @Column
    private CartStatus status;

    @Column
    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    // 아이템 목록
    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartItem> items;

    @Builder
    public Cart(Integer id, Store store, Member customer, CartStatus status, Transaction transaction, List<CartItem> items) {
        this.id = id;
        this.store = store;
        this.customer = customer;
        this.status = status;
        this.transaction = transaction;
        this.items = items;
    }
}
